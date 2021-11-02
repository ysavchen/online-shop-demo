<template>
  <v-dialog width="650px" overlay-opacity="0.7">

    <template v-slot:activator="{ on }">
      <v-btn
          slot="activator"
          depressed
          class="primary mr-4"
          v-on="on"
      >
        Register
      </v-btn>
    </template>

    <v-card>
      <v-card-text class="pt-6">
        <v-form ref="form">
          <v-text-field
            v-model="firstName"
            :rules="[rules.required]"
            label="First Name"
            maxlength="20"
          ></v-text-field>

          <v-text-field
            v-model="lastName"
            :rules="[rules.required]"
            label="Last Name"
            maxlength="20"
          ></v-text-field>

          <v-text-field
            v-model="email"
            :rules="[rules.required, rules.emailValidity]"
            label="E-mail"
          ></v-text-field>

          <v-text-field
            v-model="password"
            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, rules.minChars]"
            :type="show1 ? 'text' : 'password'"
            name="input-10-1"
            label="Password"
            hint="At least 8 characters"
            counter
            @click:append="show1 = !show1"
          ></v-text-field>

          <v-text-field
            block
            v-model="confirmPassword"
            :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
            :rules="[rules.required, passwordMatch]"
            :type="show1 ? 'text' : 'password'"
            name="input-10-1"
            label="Confirm Password"
            counter
            @click:append="show1 = !show1"
          ></v-text-field>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            depressed
            color="primary"
            :disabled="!valid"
            @click="submit"
        >
          Submit
        </v-btn>
      </v-card-actions>

    </v-card>
  </v-dialog>
</template>

<script>
import formRules from '@/mixins/form-rules'
import { mapGetters } from 'vuex'

export default {
  name: 'RegisterDialog',
  data: () => ({
    valid: true,
    firstName: null,
    lastName: null,
    email: null,
    password: null,
    confirmPassword: null,
    show1: false
  }),
  mixins: [formRules],
  computed: {
    ...mapGetters(['user']),
    passwordMatch() {
      return () =>
        this.password === this.confirmPassword || 'Password must match'
    }
  },
  methods: {
    submit() {
      const userData = {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        password: this.password
      }
      if (this.$refs.form.validate()) {
        this.$store
          .dispatch('register', userData)
          .catch(error => console.error(error))
      }
    }
  }
}
</script>