import { defineStore } from 'pinia';
import { ref } from 'vue';
import {
  type GetCompanionMessagesBody,
  requestGetCompanionConversation,
  requestSendMessage,
} from './companions';

const AUTHORIZATION_HEADER = 'Authorization';

export const useCompanionApi = defineStore('companionApi', () => {
  const authToken = ref<string | null>(null);

  function buildAuthHeader(): Record<string, string> {
    if (authToken.value) {
      return {
        [AUTHORIZATION_HEADER]: authToken.value,
      };
    }
    return {};
  }

  function updateAuthMiddleware(response: Response) {
    authToken.value = response.headers.get(AUTHORIZATION_HEADER);
    return response;
  }

  async function sendMessage(companionId: string, message: string) {
    let response = await requestSendMessage(companionId, message, {
      headers: buildAuthHeader(),
    });
    response = updateAuthMiddleware(response);
    return await response.json();
  }

  async function getCompanionConversation(
    companionId: string,
  ): Promise<GetCompanionMessagesBody> {
    let response = await requestGetCompanionConversation(companionId, {
      headers: buildAuthHeader(),
    });
    response = updateAuthMiddleware(response);
    return await response.json();
  }

  return {
    getCompanionConversation,
    sendMessage,
  };
});
