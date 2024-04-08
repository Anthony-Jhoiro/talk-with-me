import { API_ENDPOINT } from './api';

type HalResponseBody<Payload> = {
  _embedded: Payload;
};

export type Companion = {
  id: string;
  name: string;
  background: string;
  gender: string;
  species: string;
};

export type Message = {
  id: string;
  message: string;
  type: 'USER' | 'AI' | 'SYSTEM' | 'SUMMARY';
  createdAt: string;
};

const defaultUserId = 'Jhoiro';

export type CompanionConversation = {
  userId: string;
  companion: Companion;
  messages: Message[];
};

export type ListCompanionsBody = Companion[];

export type GetCompanionMessagesBody = CompanionConversation;

export const listCompanions: (
  opts?: RequestInit,
) => Promise<ListCompanionsBody> = (opts) =>
  fetch(API_ENDPOINT + '/companions', opts).then((r) => r.json());

export const getCompanionConversation: (
  companionId: string,
  opts?: RequestInit,
) => Promise<GetCompanionMessagesBody> = (companionId) =>
  fetch(
    API_ENDPOINT +
      '/companions/' +
      companionId +
      '/conversation/' +
      defaultUserId,
  ).then((r) => r.json());

export const sendMessage: (
  companionId: string,
  message: string,
) => Promise<unknown> = (companionId: string, message: string) =>
  fetch(API_ENDPOINT + '/companions/' + companionId + '/talk', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      question: message,
    }),
  });
