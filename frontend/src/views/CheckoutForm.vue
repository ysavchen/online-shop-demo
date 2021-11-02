<template>
  <v-container fluid>
    <div>
      <v-row justify="center" class="mt-5">
        <v-col class="mx-5" cols="5">
          <v-form ref="form">
            <p class="title">Delivery address</p>
            <v-text-field
                label="Name"
                v-model="name"
                :rules="[rules.required]"
            ></v-text-field>
            <v-text-field
                label="Address"
                v-model="address"
                :rules="[rules.required]"
            ></v-text-field>
            <v-text-field
                label="Phone"
                v-model="phone"
                :rules="[rules.required]"
            ></v-text-field>
            <v-text-field
                label="E-mail"
                v-model="email"
                :rules="[rules.required]"
            ></v-text-field>
          </v-form>
          <div>
            <v-btn
                color="success"
                @click="submit"
            >
              Submit
            </v-btn>
          </div>
        </v-col>

        <v-col class="mx-5" cols="5">
          <div v-if="cart.length > 0">
            <v-card outlined>
              <v-list class="py-0">
                <v-list-item-group v-for="book in cart" :key="book.id">
                  <template>
                    <v-list-item>
                      <v-list-item-avatar left tile size="65">
                        <v-img :src="book.image"></v-img>
                      </v-list-item-avatar>
                      <v-list-item-content>
                        <v-list-item-title>{{ book.title }}</v-list-item-title>
                        <v-list-item-subtitle>by {{ book.author }}</v-list-item-subtitle>
                        <v-list-item-subtitle>${{ book.price }}</v-list-item-subtitle>
                      </v-list-item-content>
                      <v-list-item-action>
                        <v-btn icon @click="deleteFromCart(book)">
                          <v-icon color="red">mdi-close</v-icon>
                        </v-btn>
                      </v-list-item-action>
                    </v-list-item>
                    <v-divider v-show="!isLastBook(book)"></v-divider>
                  </template>
                </v-list-item-group>
              </v-list>
            </v-card>
            <div>
              <p class="Subtitle-1 ml-1 mt-1">Total: ${{ total }}</p>
            </div>
          </div>
          <div v-else class="subtitle-1 text-center">No books in cart</div>
        </v-col>
      </v-row>
    </div>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex'
import commons from '@/mixins/commons'
import formRules from '@/mixins/form-rules'

export default {
  name: 'CheckoutForm',
  data: () => ({
    name: null,
    address: null,
    phone: null,
    email: null
  }),
  mixins: [commons, formRules],
  computed: {
    ...mapGetters(['cart', 'user', 'order']),
    total() {
      return this.totalPrice(this.cart)
    }
  },
  created() {
    if (this.user !== null) {
      this.name = this.user.firstName + ' ' + this.user.lastName
      this.email = this.user.email
    }
  },
  methods: {
    deleteFromCart(book) {
      this.$store.dispatch('deleteFromCart', book)
    },
    isLastBook(book) {
      return book === this.cart[this.cart.length - 1]
    },
    submit() {
      if (this.$refs.form.validate()) {
        const orderData = {
          name: this.name,
          address: this.address,
          phone: this.phone,
          email: this.email,
          books: this.cart
        }
        this.$store.dispatch('saveOrder', orderData).then(() => {
          this.$store.dispatch('emptyCart')
          this.$router.push({ name: 'order',  params: { id: this.order.id } })
        })
      }
    }
  }
}
</script>