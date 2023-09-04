import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import vuetify from './plugins/vuetify'
import { loadFonts } from './plugins/webfontloader'
import StepVerification from '@/components/StepVerification.vue'


const pinia = createPinia()
loadFonts()
const app = createApp(App);

app.component(
  'StepVerification', StepVerification
)

app
  .use(pinia)
  .use(router)
  .use(vuetify)
  .mount('#app')
  
