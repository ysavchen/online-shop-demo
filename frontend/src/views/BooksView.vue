<template>
  <v-container>
    <div>
      <v-row no-gutters>
        <template v-for="book in books">
          <v-col :key="book.id">
            <v-card max-width="250" class="pt-2 ma-2">
              <v-img
                  :src="book.image"
                  contain
                  aspect-ratio="0.8"
              ></v-img>

              <v-card-title primary-title>
                <div>
                  <p class="subtitle-2" style="height: 35px">{{ book.title }}</p>
                </div>
              </v-card-title>

              <v-card-subtitle>
                <div class="body-2">by {{ book.author }}</div>
                <div class="body-2">${{ book.price }}</div>
              </v-card-subtitle>

              <v-card-actions>
                <v-btn
                    outlined
                    small
                    @click="details(book.id)"
                >
                  Details
                </v-btn>
                <v-btn
                    outlined
                    small
                    @click="addToCart(book)"
                >
                  Add to cart
                </v-btn>
              </v-card-actions>

            </v-card>
          </v-col>
        </template>
      </v-row>
    </div>
  </v-container>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'BooksView',
  methods: {
    details(bookId) {
      this.$router.push({ name: 'bookDetails', params: { id: bookId } })
    },
    addToCart(book) {
      this.$store.dispatch('addToCart', book)
    }
  },
  computed: {
    ...mapGetters(['books'])
  },
  created() {
    this.$store.dispatch('getBooks')
  }
}
</script>
