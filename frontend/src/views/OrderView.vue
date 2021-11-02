<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="8">
        <div v-show="order !== null">
          <v-card outlined>
            <v-card-title>
              <v-icon color="green">mdi-check</v-icon>
              <div class="ml-1 black--text">Order #{{ order.id }}</div>
            </v-card-title>

            <v-divider class="mx-4"></v-divider>

            <v-card-text>
              <div class="font-weight-black">Delivery:</div>
              <div>{{ order.name }}</div>
              <div>{{ order.address }}</div>
              <div>Tel: {{ order.phone }}</div>
              <div>Email: {{ order.email }}</div>
            </v-card-text>

            <v-divider class="mx-4"></v-divider>

            <v-card-text>
              <div class="font-weight-black">Order summary:</div>
              <v-list>
                <v-list-item-group v-for="book in order.books" :key="book.id">
                  <v-list-item>
                    <v-list-item-avatar left tile size="50">
                      <v-img :src="book.image"></v-img>
                    </v-list-item-avatar>
                    <v-list-item-content>
                      <div>{{ book.title }} - ${{ book.price }}</div>
                    </v-list-item-content>
                  </v-list-item>
                </v-list-item-group>
              </v-list>
              <div>
                <p class="font-weight-black">Total: ${{ total }}</p>
              </div>
              <v-divider></v-divider>
              <div class="mt-5 text-center black--text">Thank you for order!</div>
            </v-card-text>

          </v-card>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex'
import commons from '@/mixins/commons'

export default {
  name: 'OrderView',
  mixins: [commons],
  computed: {
    ...mapGetters(['order']),
    total() {
      return this.totalPrice(this.order.books)
    }
  }
}
</script>