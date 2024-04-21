<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { type CompanionConversation } from '../api/companions';
import Message from '../components/Message.vue';
import LoadingIndicator from '../components/LoadingIndicator.vue';
import Scene3D from '../components/Scene3D.vue';
import {
  buildCompanion3dTextureLink,
  buildCompanionObjLink,
} from '../api/static';
import { useCompanionApi } from '../api/store';

const { id } = defineProps<{
  id: string;
}>();

const { getCompanionConversation, sendMessage } = useCompanionApi();

const companion = ref<CompanionConversation>();
const loadingCompanionErrorMessage = ref<string>();
const isLoadingCompanion = ref(true);

onMounted(() => {
  getCompanionConversation(id)
    .then((c) => (companion.value = c))
    .catch((e: any) => (loadingCompanionErrorMessage.value = e))
    .finally(() => (isLoadingCompanion.value = false));
});

const formInput = ref('');
const sendingMessage = ref<string | null>(null);

const messages = computed<Message[]>(() => {
  if (!companion.value) {
    return [];
  }

  if (!sendingMessage.value) {
    return [...companion.value.messages];
  }

  return [
    ...companion.value.messages,
    {
      id: 'unknown',
      message: sendingMessage.value,
      createdAt: new Date().toISOString(),
      type: 'USER',
      status: 'NOT_ARCHIVED',
    },
  ];
});

async function onNewMessage(e: any) {
  e.preventDefault();
  sendingMessage.value = formInput.value;

  await sendMessage(companion.value!.companion.id, formInput.value)
    .then((c) => (companion.value = c))
    .catch((e: any) => (loadingCompanionErrorMessage.value = e))
    .finally(() => {
      formInput.value = '';
      sendingMessage.value = null;
    });
}

const waitingForResponse = computed(() => sendingMessage.value !== null);
</script>

<template>
  <div v-if="isLoadingCompanion">
    <LoadingIndicator />
  </div>

  <p v-if="loadingCompanionErrorMessage" class="text-maroon">
    {{ loadingCompanionErrorMessage }}
  </p>

  <template v-if="companion">
    <div id="companion-header" class="flex">
      <div class="size-52">
        <Scene3D
          :object-file="buildCompanionObjLink(companion.companion.id)"
          :texture-file="buildCompanion3dTextureLink(companion.companion.id)"
        />
      </div>

      <h2 class="text-4xl font-semibold">{{ companion.companion.name }}</h2>
    </div>

    <p class="text-subtext0 border-l-4 p-5 border-subtext0 mb-5">
      {{ companion.companion.background }}
    </p>

    <section id="discussion">
      <Message
        v-for="message in messages"
        :key="message.id"
        :message="message"
      />
    </section>

    <div class="flex justify-center my-5" v-if="waitingForResponse">
      <LoadingIndicator />
    </div>

    <form @submit="onNewMessage">
      <div>
        <input
          :disabled="waitingForResponse"
          class="bg-surface1 text-text px-8 py-3 outline-4 outline-crust focus:outline-teal w-full"
          id="message-input"
          name="message"
          type="text"
          v-model="formInput"
          placeholder="Your question here"
        />
      </div>
    </form>
  </template>
</template>
