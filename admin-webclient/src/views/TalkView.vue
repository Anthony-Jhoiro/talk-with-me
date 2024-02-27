<script setup lang="ts">
import { onMounted } from 'vue';
import {
  type GetCompanionMessagesBody,
  getCompanionWithMessages,
} from '../api/companions';
import Message from '../components/Message.vue';
import { computed, ref } from 'vue';

const { id } = defineProps<{
  id: string;
}>();

const companion = ref<GetCompanionMessagesBody>();
const loadingCompanionErrorMessage = ref<string>();
const isLoadingCompanion = ref(true);

onMounted(() => {
  getCompanionWithMessages(id)
    .then((c: GetCompanionMessagesBody) => (companion.value = c))
    .catch((e: any) => (loadingCompanionErrorMessage.value = e))
    .finally(() => (isLoadingCompanion.value = false));
});

const formInput = ref('');
const sendingMessage = ref<string | null>(null);

const messages = computed(() => {
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
      message: sendingMessage,
      createdAt: new Date().toISOString(),
    },
  ];
});

async function onNewMessage(e: any) {
  e.preventDefault();
  sendingMessage.value = formInput.value;
  //await sendMessage(companion.value!.companion.id, formInput.value);

  // Reset input
  formInput.value = '';
  //await refresh();
  sendingMessage.value = null;
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
        <Scene3D object-file="/vulpis.obj" texture-file="/vulpis.png" />
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

<style scoped></style>
