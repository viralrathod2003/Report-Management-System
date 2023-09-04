<template>
  <div>
    <v-app>
      <!-- side navigation  -->
      <v-navigation-drawer v-model="drawer" elevation="0" class="side-bar" style=" height: 98.5vh;">
        <v-toolbar class="user">
          <v-app-bar-nav-title class="username">{{ username }}</v-app-bar-nav-title>
          <v-app-bar-nav-icon @click.stop="drawer = !drawer" style="margin-left: auto;" />
        </v-toolbar>


        <v-list>
          <v-list-item v-for="(item, i) in items" :key="i" :value="item" color="primary" shaped rounded="xl"
            @click="val = item.val" style="margin: 10px;">
            <template v-slot:prepend>
              <v-icon class="mr-4" style="color: white;">{{ item.icon }}</v-icon>
            </template>
            <v-list-item-title class="list-item" @click="val = item.val">{{ item.title }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-navigation-drawer>

      <!-- top nav bar  -->
      <v-app-bar elevation="0">
        <div class="nav-bar">
          <div class="toggle-button">
            <v-btn v-if="!drawer" @click.stop="drawer = !drawer" icon>
              <v-icon class="mr-4">mdi-menu</v-icon>
            </v-btn>
          </div>
          <div class="logout-button" style="position:relative;left:10px;">
            <v-btn @click="logout" icon>
              <v-icon class="mr-4">mdi-logout</v-icon>
            </v-btn>
          </div>
        </div>
      </v-app-bar>

      <!-- main content  -->
      <v-main>
        <div class="content">

          <!-- info  -->
          <v-row>
            <v-col cols="12" sm="6" md="3">
              <v-card elevation="0" class="col-content" style="background-color: rgb(20, 177, 20); color: white; padding: 30px 0px;" @click="popup = 1">
                <v-card-title>
                  <v-icon class="mr-4" >mdi-clock</v-icon> Pending : {{ pending }}
                </v-card-title>
              </v-card>
            </v-col>

            <v-col cols="12" sm="6" md="3">
              <v-card elevation="0" class="col-content" style="background-color: rgb(255, 42, 42); color: white; padding: 30px 0px;" @click="popup = 2">
                <v-card-title>
                  <v-icon class="mr-4" >mdi-check-circle</v-icon> Approved : {{ approved }}
                </v-card-title>
              </v-card>
            </v-col>

            <v-col cols="12" sm="6" md="3">
              <v-card elevation="0" class="col-content" style="background-color: rgb(248, 40, 234); color: white; padding: 30px 0px;" @click="popup = 3">
                <v-card-title>
                  <v-icon class="mr-4" >mdi-close-circle</v-icon> Rejected : {{ rejected }}
                </v-card-title>
              </v-card>
            </v-col>

            <!-- 3 -->
            <v-col cols="12" sm="6" md="3">
              <v-card elevation="0" class="col-content" style="background-color: rgb(1, 170, 165); color: white; padding: 30px 0px;" @click="val = 3">
                <v-card-title>
                  <v-icon  class="mr-4"> mdi-plus</v-icon> Add New Request
                </v-card-title>
              </v-card>
            </v-col>
          </v-row>


          <!-- 3 -->
          <div v-if="val === 3">
            <v-dialog v-model="show" max-width="800px" >
              <v-card style="background-color: #CBB279; border-radius: 20px;">
                <v-toolbar style="background-color: #CBB279; border-radius: 20px;">
                  <v-toolbar-title style="font-size:x-large;">Add new request</v-toolbar-title>
                  <v-btn icon @click="val = 0">
                    <v-icon class="mr-4">mdi-close</v-icon>
                  </v-btn>
                </v-toolbar>
                <AddRequest />
              </v-card>
            </v-dialog>
          </div>

          <!-- displaying table  -->
          <v-card width="100%" elevation="0">
            <v-card-title style="text-align: center;">Request Details</v-card-title>
            <RequestView />
          </v-card>
        </div>

      </v-main>
    </v-app>
  </div>
</template>
<script>

import image from '@/assets/logo.png';
import StepVerification from '@/components/StepVerification.vue';
import RequestView from './RequestView.vue';
import AddRequest from '@/views/employee/AddRequest.vue'
import { useAuthStore } from '@/stores/auth.js';
import router from '@/router/index.js';
import axios from '@/plugins/axios';

export default {

  components: {
    StepVerification,
    RequestView,
    AddRequest,
  },
  data() {
    return {
      pending: 0,
      approved: 0,
      rejected: 0,
      drawer: true,
      show: true,
      val: 0,
      items: [
        { title: 'Request Details', val: 1, icon: 'mdi-file' },
        { title: 'Mail History', val: 2, icon: 'mdi-email' },
        { title: 'Add Request', val: 3, icon: 'mdi-plus' },
      ],
      search: '',
      add_request: false,
      image: image,
      expand: false,
      username: localStorage.getItem('username'),
    };
  },
  methods: {
    logout() {
      useAuthStore().logout();
      router.push('/');
    }
  },
  async created() {
    try {
      const response = await axios.get('http://localhost:8080/user/requests');
      const data = response.data;
      data.forEach((request) => {
        if (request.requestStatus === 200) {
          this.approved += 1;
        } else if (request.requestStatus === 100) {
          this.rejected += 1;
        } else {
          this.pending += 1;
        }
      });
    } catch (error) {
      console.error(error);
    }

  },

};

</script>
  
<style scoped>
.toggle-button {
  width: 100%;
  cursor: pointer;

}

.nav-bar {
  background-color:  hsl(193, 12%, 62%);
  display: flex;
  width: 100%;
  border-radius: 10px;
  padding: 0px 10px 0px 0px;
  margin: 0px 5px 0px 10px;

}

.content {
  padding: 1vw;
  border-radius: 15px;
  width: 100%;
  height: 300px;
  overflow: auto;
  height: 90vh;
}

.content::-webkit-scrollbar {
  width: 0;
  background-color: transparent;
}

.side-bar {
  padding: 5px;
  background-color:  hsl(193, 12%, 62%);
  width: auto;
  border-radius: 15px;
  margin: 5px 5px 5px 5px;

}

.user {
  border-radius: 15px;
  padding: 0 0 0 15px;
  margin: 0px;
}

.col-content {
  display: flex;
  justify-content: center;
  border-radius: 15px;
  padding: 10px;
  margin: 20px;
}
</style>
  