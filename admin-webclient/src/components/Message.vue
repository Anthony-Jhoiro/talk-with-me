<script setup lang="ts">
import type { Message } from '../api/companions';

const { message } = defineProps<{
  message: Message;
}>();

const additionalClasses: Record<Message['type'], string> = {
  USER: 'border-blue',
  AI: 'border-green',
  SYSTEM: 'border-pink',
  SUMMARY: 'border-subtext0 text-subtext0',
};
</script>

<template>
  <div
    class="border-l-4 px-5 py-2 my-1"
    :class="additionalClasses[message.type]"
  >
    <p>{{ message.message }}</p>
  </div>
</template>

<style scoped></style>
