import {API_ENDPOINT} from "./api";

export function buildStaticAssetLink(path: string) {
  return API_ENDPOINT + path;
}

export function buildCompanionObjLink(id: string) {
  return API_ENDPOINT + '/neighbors/' + id + '/' + id + '.obj';
}

export function buildCompanion3dTextureLink(id: string) {
  return API_ENDPOINT + '/neighbors/' + id + '/' + id + '.png';
}
