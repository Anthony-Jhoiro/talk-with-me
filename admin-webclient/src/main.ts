import router from './router';

import { createApp } from 'vue';
import App from './app/App.vue';
import './index.css';
import { createPinia } from 'pinia';

const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount('#root');
