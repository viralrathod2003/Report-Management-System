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

            <!-- main content  -->
            <v-main>
                <div class="content">

                    <!-- common details -->
                    <v-row>
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(20, 177, 20); color: white; padding: 30px 0px;" @click="popup = 1">
                                <v-card-title>
                                    <v-icon color="black">mdi-account-plus</v-icon> Add Approver
                                </v-card-title>
                            </v-card>
                        </v-col>
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(255, 42, 42); color: white; padding: 30px 0px;" @click="popup = 2">
                                <v-card-title>
                                    <v-icon color="black">mdi-account-group</v-icon> Add Department
                                </v-card-title>
                            </v-card>
                        </v-col>
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(248, 40, 234); color: white; padding: 30px 0px;" @click="popup = 3">
                                <v-card-title>
                                    <v-icon color="black">mdi-account-plus</v-icon> Add Employee
                                </v-card-title>
                            </v-card>
                        </v-col>
                        <v-col cols="12" sm="6" md="3">
                            <v-card elevation="0" class="col-content" style="background-color: rgb(1, 170, 165); color: white; padding: 30px 0px;" @click="popup = 4">
                                <v-card-title>
                                    <v-icon color="black">mdi-file-plus</v-icon> Add Report
                                </v-card-title>
                            </v-card>
                        </v-col>
                    </v-row>

                    <!-- body content  -->

                    <!-- 1.  -->
                    <div v-if="popup === 1">
                        <v-dialog v-model="show" max-width="800px" color="black">
                            <v-card style="background-color: #CBB279; border-radius: 20px;">
                                <v-toolbar style="background-color: #CBB279; border-radius: 20px;">
                                    <v-toolbar-title style="font-size:x-large;">Add Approver</v-toolbar-title>
                                    <v-btn icon @click="popup = 0">
                                        <v-icon>mdi-close</v-icon>
                                    </v-btn>
                                </v-toolbar>
                                <AddApprover />
                            </v-card>
                        </v-dialog>
                    </div>

                    <!-- 3. -->
                    <div v-if="popup === 2">
                        <v-dialog v-model="show" max-width="800px" color="black">
                            <v-card style="background-color: #CBB279; border-radius: 20px;" elevation="0">
                                <v-toolbar style="background-color: #CBB279; border-radius: 20px;">
                                    <v-toolbar-title style="font-size:x-large;">Add Department</v-toolbar-title>
                                    <v-btn icon @click="popup = 0">
                                        <v-icon>mdi-close</v-icon>
                                    </v-btn>
                                </v-toolbar>
                                <AddDepartment />
                            </v-card>
                        </v-dialog>
                    </div>

                    <!-- 4. -->
                    <div v-if="popup === 3">
                        <v-dialog v-model="show" max-width="800px" color="black">
                            <v-card style="background-color: #CBB279; border-radius: 20px;" elevation="0">
                                <v-toolbar style="background-color: #CBB279; border-radius: 20px;">
                                    <v-toolbar-title style="font-size:x-large;">Add Employee</v-toolbar-title>
                                    <v-btn icon @click="popup = 0">
                                        <v-icon>mdi-close</v-icon>
                                    </v-btn>
                                </v-toolbar>
                                <AddEmployee />
                            </v-card>
                        </v-dialog>
                    </div>

                    <!-- 5. -->
                    <div v-if="popup === 4">
                        <v-dialog v-model="show" max-width="800px" color="black">
                            <v-card style="background-color: #CBB279; border-radius: 20px;" elevation="0">
                                <v-toolbar style="background-color: #CBB279; border-radius: 20px;">
                                    <v-toolbar-title style="font-size:x-large;">Add Report</v-toolbar-title>
                                    <v-btn icon @click="popup = 0">
                                        <v-icon>mdi-close</v-icon>
                                    </v-btn>
                                </v-toolbar>
                                <AddReport />
                            </v-card>
                        </v-dialog>
                    </div>

                    <!-- 1 -->
                    <div v-if="val === 1">
                        <ApproverView />
                    </div>

                    <!-- 2 -->
                    <div v-if="val === 2">
                        <DepartmentView />
                    </div>

                    <!-- 3 -->
                    <div v-if="val === 3">
                        <ReportsView />
                    </div>

                    <!-- 4  -->
                    <div v-if="val === 4">
                        <EmployeeView />
                    </div>

                    <!-- 5 -->
                    <div v-if="val === 5">
                        <RequestView />
                    </div>
                </div>

            </v-main>
        </v-app>
    </div>
</template>
  
<script>

// view
import image from '@/assets/logo.png';
import DepartmentView from '@/views/admin/view/DepartmentView.vue';
import RequestView from '@/views/admin/view/RequestView.vue';
import ApproverView from '@/views/admin/view/ApproverView.vue';
import EmployeeView from '@/views/admin/view/EmployeeView.vue';
import ReportsView from '@/views/admin/view/ReportsView.vue';

// add 
import AddApprover from '@/views/admin/add/AddApprover.vue';
import AddDepartment from '@/views/admin/add/AddDepartment.vue';
import AddEmployee from '@/views/admin/add/AddEmployee.vue';
import AddReport from '@/views/admin/add/AddReport.vue';

// modify
import EditApprover from '@/views/admin/modify/EditApprover.vue';

//common components
import StepVerification from '@/components/StepVerification.vue';
import { useAuthStore } from '@/stores/auth.js';
import router from '@/router/index.js';

export default {
    components: {
        StepVerification,
        RequestView,

        DepartmentView,
        EmployeeView,
        ApproverView,
        ReportsView,

        EditApprover,

        AddApprover,
        AddReport,
        AddDepartment,
        AddEmployee,
    },
    data() {
        return {
            drawer: true,
            val: 1,
            popup: 0,
            show: true,
            items: [
                { title: 'Approvers', val: 1, icon: 'mdi-check-circle' },
                { title: 'Departments', val: 2, icon: 'mdi-account-group' },
                { title: 'Reports', val: 3, icon: 'mdi-file-document' },
                { title: 'Employees', val: 4, icon: 'mdi-account' },
                { title: 'Requests', val: 5, icon: 'mdi-calendar-clock' },

            ],
            username: localStorage.getItem("username"),
            search: '',
            image: image,
            expand: false,
        };
    },
    methods: {
        logout() {
            useAuthStore().logout();
            router.push('/');
        }
    }
};
</script>
  
<style scoped>
.toggle-button {
    width: 100%;
    cursor: pointer;

}

.nav-bar {
    background-color: hsl(193, 12%, 62%);
    color: white;
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
    background-color: hsl(193, 12%, 62%);
    color: white;
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
  