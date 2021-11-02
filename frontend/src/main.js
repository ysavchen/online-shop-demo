import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'
import vuetify from './plugins/vuetify'

Vue.config.productionTip = false
Vue.use(VueAxios, axios)
axios.defaults.baseURL = '/api'
axios.defaults.headers.common['Accept'] = 'application/json'

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
