<template>
  <v-dialog width="550px" overlay-opacity="0.7">
    <template v-slot:activator="{ on }">
      <v-btn
          depressed
          class="primary mr-4"
          v-on="on"
      >
        Login
      </v-btn>
    </template>
    <v-card>
      <v-toolbar color="primary" dark flat>
        <v-toolbar-title>Login</v-toolbar-title>
        <v-spacer></v-spacer>
      </v-toolbar>
      <v-card-text class="mt-7">
        <v-form>
          <v-text-field
            label="Email"
            name="email"
            prepend-icon="mdi-email"
            type="text"
            v-model="email"
          ></v-text-field>

          <v-text-field
            id="password"
            label="Password"
            name="password"
            prepend-icon="mdi-lock"
            type="password"
            v-model="password"
          ></v-text-field>
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            depressed
            color="primary"
            @click="submit"
        >
          Submit
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'LoginDialog',
  data: () => ({
    email: null,
    password: null
  }),
  computed: {
    ...mapGetters(['user'])
  },
  methods: {
    submit() {
      const userData = {
        email: this.email,
        password: this.password
      }
      this.$store
        .dispatch('login', userData)
        .catch(error => console.error(error))
    }
  }
}
</script>