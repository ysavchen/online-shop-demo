<template>
  <v-app>
    <app-navbar/>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import AppNavbar from './components/AppNavbar'

export default {
  name: 'App',

  components: {
    'app-navbar': AppNavbar
  }
}
</script>
