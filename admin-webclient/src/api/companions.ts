import { API_ENDPOINT } from './api';

type HalResponseBody<Payload> = {
  _embedded: Payload;
};

export type Companion = {
  id: string;
  name: string;
  background: string;
};

export type Message = {
  id: string;
  message: string;
  type: 'USER' | 'AI' | 'SYSTEM' | 'SUMMARY';
  createdAt: string;
};

export type CompanionWithMessages = {
  companion: Companion;
  messages: Message[];
};

export type ListCompanionsBody = HalResponseBody<{ companions: Companion[] }>;

export type GetCompanionMessagesBody = CompanionWithMessages;

export const listCompanions: (
  opts?: RequestInit,
) => Promise<ListCompanionsBody> = (opts) =>
  fetch(API_ENDPOINT + '/companions', opts).then((r) => r.json());

export const getCompanionWithMessages: (
  companionId: string,
  opts?: RequestInit,
) => Promise<GetCompanionMessagesBody> = (companionId) =>
  fetch(
    API_ENDPOINT + '/companions/' + companionId + '?projection=messages',
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
