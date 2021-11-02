<template>
  <v-app-bar app light>
    <v-toolbar-title>
      <router-link
          style="text-decoration: none; color: inherit;"
          to="/"
      >
        {{ sitename }}
      </router-link>
    </v-toolbar-title>

    <v-spacer></v-spacer>

    <div v-if="!isAuthenticated">
      <register-dialog />
      <login-dialog />
    </div>
    <div v-else>
      <template>
        <div class="text-center">
          <v-menu offset-y>
            <template v-slot:activator="{ on }">
              <v-btn
                  class="mr-2"
                  text
                  color="primary"
                  v-on="on"
              >
                {{ user.firstName }} {{ user.lastName }}
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="orders">
                <v-list-item-title class="text-center">Orders</v-list-item-title>
              </v-list-item>
              <v-list-item @click="logout">
                <v-list-item-title class="text-center">Logout</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
      </template>
    </div>

    <div>
      <v-btn icon @click="openCheckout">
        {{ cartItemCount }}
        <v-icon>mdi-cart-outline</v-icon>
      </v-btn>
    </div>
  </v-app-bar>
</template>

<script>
import { mapGetters } from 'vuex'
import LoginDialog from '@/components/LoginDialog'
import RegisterDialog from '@/components/RegisterDialog'

export default {
  name: 'AppNavbar',
  data: () => ({
    sitename: 'Online Store'
  }),
  components: {
    'login-dialog': LoginDialog,
    'register-dialog': RegisterDialog
  },
  computed: {
    ...mapGetters([
        'cart',
        'token',
        'user',
        'isAuthenticated'
    ]),
    cartItemCount() {
      return this.cart.length
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('logout')
      this.$router.push({ name: 'books' })
    },
    orders() {
      const userId = this.user.id
      const userToken = this.token
      this.$store
        .dispatch('getUserOrders', { userId, userToken })
        .then(() => {
          this.$router.push({ name: 'userOrders', params: { id: userId } })
        })
        .catch(error => console.log(error))
    },
    openCheckout() {
      this.$router.push({ name: 'checkout' })
    }
  }
}
</script>