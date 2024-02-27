<script setup lang="ts">
import { OBJLoader } from 'three/addons/loaders/OBJLoader.js';
import { Scene, TextureLoader, Mesh, NearestFilter } from 'three';

const { objectFile, textureFile } = defineProps<{
  objectFile: string;
  textureFile: string;
}>();

const scene = new Scene();
const loader = new OBJLoader();

const texture = new TextureLoader().load(textureFile);

// Disable blur effect on pixelated textures
texture.magFilter = NearestFilter;
texture.minFilter = NearestFilter;

loader.load(
  // resource URL
  objectFile,
  // called when resource is loaded
  function (object) {
    object.traverse((child) => {
      if (child instanceof Mesh) {
        child.material.map = texture;
      }
    });

    scene.add(object);
  },
  // called when loading is in progresses
  function (xhr) {
    console.log((xhr.loaded / xhr.total) * 100 + '% loaded');
  },
  // called when loading has errors
  function (error) {
    console.log('An error happened');
  },
);
</script>

<template>
  <primitive :object="scene" />
</template>

<style scoped></style>
