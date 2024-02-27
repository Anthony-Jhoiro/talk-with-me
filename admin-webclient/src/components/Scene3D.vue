<script setup lang="ts">
import Object3D from './Object3D.vue';
import {TresCanvas} from "@tresjs/core"

const { objectFile, textureFile } = defineProps<{
  objectFile: string;
  textureFile: string;
}>();
</script>

<template>
  <TresCanvas :alpha="true">
    <TresPerspectiveCamera :position="[0.4, 1.5, -2]" :look-at="[0, 0.75, 0]" />
    <Suspense>
      <!-- Object is handled in another component to avoid server-side rendering issues -->
      <Object3D :object-file="objectFile" :texture-file="textureFile" />
    </Suspense>
    <TresAmbientLight :intensity="2" />
  </TresCanvas>
</template>

<style scoped></style>
