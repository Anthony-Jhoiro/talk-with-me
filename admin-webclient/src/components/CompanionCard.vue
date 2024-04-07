<script setup lang="ts">
import type {Companion} from '../api/companions';
import Scene3D from './Scene3D.vue';
import {buildCompanion3dTextureLink, buildCompanionObjLink} from "../api/static";

const {companion} = defineProps<{
  companion: Companion;
}>();
</script>

<template>
  <div
    class="border-teal border-2 rounded p-5 flex flex-col h-full w-full space-y-5"
  >
    <div class="h-80 w-full bg-img-minecraft-bg bg-center bg-cover rounded">
      <Scene3D :object-file="buildCompanionObjLink(companion.id)"
               :texture-file="buildCompanion3dTextureLink(companion.id)"/>
    </div>
    <h3 class="font-bold">{{ companion.name }}</h3>

    <div class="text-subtext0 overflow-y-auto h-full">
      {{ companion.background }}
    </div>

    <div>
      <slot name="action-row"/>
    </div>
  </div>
</template>

<style scoped></style>
