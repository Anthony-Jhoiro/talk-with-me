<script setup lang="ts">
import { ChatBubbleOvalLeftEllipsisIcon } from '@heroicons/vue/24/solid';
import { onMounted } from '@vue/runtime-core';
import { ref } from 'vue';
import { type Companion, listCompanions } from '../api/companions';
import LoadingIndicator from '../components/LoadingIndicator.vue';
import CompanionCard from '../components/CompanionCard.vue';

const pending = ref(true);
const errorMessage = ref<string|null>(null);
const companions = ref<Companion[]|null>(null);

onMounted(() => {
  listCompanions()
    .then(res => companions.value = res._embedded.companions)
    .catch(e => errorMessage.value = e)
    .finally(() => pending.value = false)
})



</script>

<template>
  <div v-if="pending">
    <LoadingIndicator />
  </div>

  <p v-if="errorMessage" class="text-maroon">
    {{ errorMessage }}
  </p>

  <section
    id="companions"
    v-if="companions"
    class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-3"
  >
    <div class="h-[500px]" v-for="companion in companions">
      <CompanionCard :companion="companion">
        <template #action-row>
          <div class="flex justify-end gap-3">
            <router-link :to="{name: 'talk', params: {id: companion.id}}"
              class="py-3 px-5 bg-teal hover:bg-teal-700 text-white flex items-center gap-2 rounded transition-colors"
            >
              <ChatBubbleOvalLeftEllipsisIcon class="h-5" />
              <span>Start Talking</span>
            </router-link>
          </div>
        </template>
      </CompanionCard>
    </div>
  </section>
</template>

<style scoped></style>
