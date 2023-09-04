<template>
    <div id="app">
        <v-app>

            <!-- side navigation  -->
            <v-navigation-drawer v-model="drawer" elevation="0" class="side-bar" style="height: auto;">
                <v-toolbar class="user">
                    <v-app-bar-nav-title class="username">{{ username }}</v-app-bar-nav-title>
                    <v-app-bar-nav-icon @click.stop="drawer = !drawer" style="margin-left: auto;" />
                </v-toolbar>

                <v-list>
                    <v-list-item v-for="(item, i) in items" :key="i" :value="item" color="primary" shaped rounded="xl"
                        @click="val = item.val" style="margin: 10px;">
                        <template v-slot:prepend>
                            <v-icon>{{ item.icon }}</v-icon>
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
                            <v-icon>mdi-menu</v-icon>
                        </v-btn>
                    </div>
                    <div class="logout-button" style="position:relative;left:10px;">
                        <v-btn @click="logout" icon>
                            <v-icon>mdi-logout</v-icon>
                        </v-btn>
                    </div>
                </div>
            </v-app-bar>
            <v-main>
                <div class="content">

                    <!-- common details  -->
                    <v-row>
                        <!-- 1 -->
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(20, 177, 20); color: white; padding: 30px 0px;" @click="popup = 1">
                                <v-card-title>
                                    <v-icon color="black">mdi-clock-outline</v-icon> Pending: {{ pending }}
                                </v-card-title>
                            </v-card>
                        </v-col>

                        <!-- 2 -->
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(255, 42, 42); color: white; padding: 30px 0px;" @click="popup = 2">
                                <v-card-title>
                                    <v-icon color="black">mdi-check-circle-outline</v-icon> Approved: {{ approved }}
                                </v-card-title>
                            </v-card>
                        </v-col>

                        <!-- 3 -->
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(248, 40, 234); color: white; padding: 30px 0px;" @click="popup = 3">
                                <v-card-title>
                                    <v-icon color="black">mdi-close-circle-outline</v-icon> Rejected: {{ rejected }}
                                </v-card-title>
                            </v-card>
                        </v-col>

                        <!-- 4 -->
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(1, 170, 165); color: white; padding: 30px 0px;" @click="popup = 4">
                                <v-card-title>
                                    <v-icon color="black">mdi-history</v-icon> Verified: {{ totalVarified }}
                                </v-card-title>
                            </v-card>
                        </v-col>
                    </v-row>


                    <!-- 5 -->
                    <div v-if="val === 1">
                        <v-card width="100%" elevation="0">
                            <v-card-title style="text-align: center;">Pending Request</v-card-title>
                            <PendingRequest />
                        </v-card>
                    </div>

                    <!-- 6 -->
                    <div v-if="val === 2">
                        <v-card width="100%" elevation="0">
                            <v-card-title style="text-align: center;">History</v-card-title>
                            <ApproverHistory />
                        </v-card>
                    </div>
                </div>
            </v-main>
        </v-app>
    </div>
</template>

<script>
import StepVerification from '@/components/StepVerification.vue';
import PendingRequest from '@/views/approvers/PendingRequest.vue';
import { useAuthStore } from '@/stores/auth.js';
import ApproverHistory from './ApproverHistory.vue';
import router from '@/router/index.js';
import axios from '@/plugins/axios';
export default {
    components: {
        StepVerification,
        PendingRequest,
        useAuthStore,
        ApproverHistory,
    },
    data() {
        return {
            totalVarified: 0,
            pending: 0,
            approved: 0,
            rejected: 0,
            drawer: true,
            val: 1,
            show: true,
            items: [
                { title: 'Pending Request', val: 1, icon: 'mdi-clock-outline' },
                { title: 'History', val: 2, icon: 'mdi-history' },
            ],
            username: localStorage.getItem("username"),
            search: '',
            expand: false,
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
            const [pendingRequestResponse, requestHistoryResponse] = await Promise.all([
                axios.get('http://localhost:8080/approver/requests/pending'),
                axios.get('http://localhost:8080/approver/history')
            ]);

            const pendingRequestData = pendingRequestResponse.data;
            const requestHistoryData = requestHistoryResponse.data;
            console.log(pendingRequestData)
            this.pending = pendingRequestData.length;

            requestHistoryData.forEach((request) => {
                if (request.actionTaken) {
                    this.approved += 1;
                } else {
                    this.rejected += 1;
                }
            });
            this.totalVarified = requestHistoryData.length;
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
    background-color: hsl(193, 12%, 62%);
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
    /* Set desired height */
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
    border-radius: 15px;
    padding: 10px;
    margin: 20px;
}
</style>
  