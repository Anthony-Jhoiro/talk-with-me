<script setup lang="ts">
import { computed, ref } from 'vue';
import { RouterView } from 'vue-router';
import ThemeSwitch from '../components/ThemeSwitch.vue';


const darkMode = ref(false);

const themeClass = computed(() => (darkMode.value ? 'macchiato' : 'latte'));
</script>

<template>
  <div
    class="bg-base overflow-x-hidden overflow-y-scroll w-screen h-screen"
    :class="themeClass"
  >
    <header class="w-screen">
      <nav class="flex px-8 py-3 bg-crust justify-between items-center">
        <router-link :to="{name: 'home'}">
          <h1 class="text-text text-2xl">Talk With Me</h1>
        </router-link>

        <ThemeSwitch v-model="darkMode"/>
      </nav>
    </header>

    <main class="h-full container mx-auto text-text bg-surface0 py-5 px-8">
      <RouterView />
    </main>
  </div>
</template>
