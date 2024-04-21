import { SERVER_BASE_URL } from './api';

export function buildStaticAssetLink(path: string) {
  return SERVER_BASE_URL + path;
}

export function buildCompanionObjLink(id: string) {
  return buildStaticAssetLink('/neighbors/' + id + '/' + id + '.obj');
}

export function buildCompanion3dTextureLink(id: string) {
  return buildStaticAssetLink('/neighbors/' + id + '/' + id + '.png');
}
