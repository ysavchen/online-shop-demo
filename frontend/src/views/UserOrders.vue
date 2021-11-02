<template>
  <v-container>
    <div v-if="userOrders.length > 0">
    <template v-for="order in userOrders">
      <v-row :key="order.id" justify="center">
        <v-col cols="10">
          <v-card>
            <v-card-title class="grey lighten-4 py-3" style="font-size: 1.1em;">
              Order #{{ order.id }}
              <br />
              {{ formattedDate(order.createdAt) }}
              <v-spacer></v-spacer>
              Payment: ${{ total(order) }}
            </v-card-title>

            <v-list-item>
              <div id="delivery">
                <strong>Delivery:</strong>
                <div>{{ order.name }}</div>
                <div>{{ order.address }}</div>
              </div>
              <v-spacer></v-spacer>

              <div v-for="book in order.books" :key="book.id" class="mx-2 my-2">
                <router-link :to="{ name: 'bookDetails', params: { id: book.id }}">
                  <v-img
                      :src="book.image"
                      contain
                      max-height="180"
                      max-width="80"
                  ></v-img>
                </router-link>
              </div>
            </v-list-item>

          </v-card>
        </v-col>
      </v-row>
    </template>
    </div>
    <div v-else class="subtitle-1 text-center">No orders available</div>
  </v-container>
</template>

<script>
import moment from 'moment'
import { mapGetters } from 'vuex'
import commons from '@/mixins/commons'

export default {
  name: 'UserOrders',
  mixins: [commons],
  computed: {
    ...mapGetters(['userOrders'])
  },
  methods: {
    total(order) {
      return this.totalPrice(order.books)
    },
    formattedDate(date) {
      return moment(new Date(date)).format('DD.MM.YYYY HH:mm')
    }
  }
}
</script>

<style>
#delivery {
  position: absolute;
  top: 3px;
}
</style>