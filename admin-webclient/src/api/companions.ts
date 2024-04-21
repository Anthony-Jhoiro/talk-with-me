import { API_ENDPOINT } from './api';

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

export const requestGetCompanionConversation = (
  companionId: string,
  opts?: RequestInit,
) => fetch(API_ENDPOINT + '/companions/' + companionId + '/conversation', opts);

export const requestSendMessage = (
  companionId: string,
  message: string,
  opts?: RequestInit,
) =>
  fetch(API_ENDPOINT + '/companions/' + companionId + '/conversation', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...opts?.headers,
    },
    body: JSON.stringify({
      question: message,
    }),
  });
