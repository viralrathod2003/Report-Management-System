<template>
    <!-- filter section  -->
    <div class="filter-section">
        <label for="departmentSelect">Department:</label>
        <select class="custom-dropdown" v-model="selectedDepartment">
            <option value="">All</option>
            <option v-for="department in departments" :value="department.departmentId" :key="department.departmentId">
                {{ department.departmentName }}
            </option>
        </select>
        <label for="levelSelect">Level:</label>
        <select class="custom-dropdown" v-model="selectedLevel">
            <option value="">All</option>
            <option v-for="level in levels" :value="level" :key="level">
                {{ level }}
            </option>
        </select>
    </div>

    <!-- table content -->
    <v-card width="100%" class="overflow-auto table" elevation="0">
        <div class="search-bar">
            <v-icon class="search-icon">mdi-magnify</v-icon>
            <input type="text" v-model="searchQuery" placeholder="Search" class="search-input" />
            <button class="search-button" @click="performSearch">Search</button>
        </div>
        <table class="my-table">
            <thead>
                <tr>
                    <th v-for="header in headers" :key="header.key">{{ header.label }}</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in filteredApprovers" :key="item.id">
                    <td v-for="header in headers" :key="header.key">{{ item[header.key] }}</td>
                        <v-icon class="view-icon" @click="viewApprover(item['username'])">mdi-eye</v-icon>
                        <v-icon class="action-icon" @click="editApprover(item)">mdi-pencil</v-icon>
                </tr>
            </tbody>
        </table>
    </v-card>

    <div v-if="val===1">
        <v-dialog v-model="show" max-width="70%" color="black">
            <v-card style="background-color: #CBB179; border-radius: 20px;">
                <v-toolbar style="background-color: #0ae489; border-radius: 20px 20px 0px 0px; text-align: center;">
                    <v-toolbar-title style="font-size:x-large;">Approver History</v-toolbar-title>
                    <v-btn icon @click="closeDialog">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-toolbar>
                <ApproverHistory :selectedId="id" />
            </v-card>
        </v-dialog>
    </div>

    <div v-if="val===2">
        <v-dialog v-model="show" max-width="500px" color="black">
            <v-card style="border-radius: 20px;">
                <v-toolbar style="background-color: #0ae489; border-radius: 20px 20px 0px 0px; text-align: center;">
                    <v-toolbar-title style="font-size:x-large;">Update Approver Details</v-toolbar-title>
                    <v-btn icon @click="closeDialog">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-toolbar>
                <EditApprover :approver="approver"/>
            </v-card>
        </v-dialog>
    </div>
</template>
  
<script>
import ApproverHistory from '@/views/admin/view/ApproverHistory.vue'
import EditApprover from '@/views/admin/modify/EditApprover.vue';
import axios from '@/plugins/axios';

export default {
    components: {
        ApproverHistory,
        EditApprover,
    },
    data() {
        return {
            show: true,
            searchQuery: '',
            id: '',
            approver:'',
            val: 0,
            headers: [
                { label: 'Username', key: 'username' },
                { label: 'Name', key: 'name' },
                { label: 'email', key: 'email'},
                { label: 'Department Name', key: 'departmentName' },
                { label: 'Approver Level', key: 'userLevel' },

            ],
            departments: null,
            allApprovers: [],
            info: null,
            selectedDepartment: '',
            selectedLevel: '',
        };
    },
    async created() {
        try {
            const { data: departments } = await axios.get('http://localhost:8080/departments');
            const { data: allApprovers } = await axios.get('http://localhost:8080/admin/approvers');

            this.departments = departments, this.allApprovers = allApprovers, this.info = {};
            for (const department of this.departments)
                this.info[department.departmentId] = { userLevel: {} };

            for (const approver of this.allApprovers) {
                const { departmentId, userLevel } = approver;
                if (this.info[departmentId] && this.info[departmentId].userLevel) {
                    if (!this.info[departmentId].userLevel[userLevel])
                        this.info[departmentId].userLevel[userLevel] = [];
                    this.info[departmentId].userLevel[userLevel].push(approver);
                }
            }
        } catch (error) {
            console.error(error);
        }
    },
    computed: {

        levels() {
            const uniqueLevels = [...new Set(this.allApprovers.map(approver => approver.userLevel))];
            return uniqueLevels;
        },

        filteredApprovers() {
            const selectedDepartmentId = this.selectedDepartment;
            const selectedLevel = this.selectedLevel;
            const searchQuery = this.searchQuery ? this.searchQuery.toLowerCase() : '';

            return this.allApprovers.filter(approver => {
                const departmentMatch = !selectedDepartmentId || approver.departmentId === selectedDepartmentId;
                const levelMatch = !selectedLevel || approver.userLevel === selectedLevel;
                const searchMatch = Object.values(approver).some(value =>
                    value.toString().toLowerCase().includes(searchQuery)
                );

                return departmentMatch && levelMatch && searchMatch;
            });
        },

    },
    methods: {
        editApprover(item) {
            this.val = 2;
            this.approver=item;
        },
        viewApprover(id) {
            this.val = 1;
            this.id = id;
        },
        closeDialog() {
            this.val = 0;
        }
    }
};
</script>
  
<style scoped>

.view-icon {
  margin: 10px 20px 0px 0px;
  color: #0ad568;
  cursor: pointer;
}

.action-icon {
  margin: 10px 0px 0px 20px;
  color: #efcc05;
  cursor: pointer;
}

.filter-section {
    display: flex;
    align-items: center;
    margin: 10px;
}

.custom-dropdown {
    padding: 5px 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
    background-color: #bae5dd;
    color: #333;
    cursor: pointer;
    width: 100%;
    margin: 10px;
}

.search-bar {
    z-index: 100;
    position: relative;
    display: flex;
    width: 100%;
    align-items: center;
    padding: 8px;
    background-color: #f0f0f0;
}

.search-icon {
    margin-right: 8px;
    color: #999;
}

.search-input {
    flex: 1;
    padding: 6px 10px;
    border: none;
    border-radius: 4px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    font-size: 14px;
    outline: none;
}

.search-input::placeholder {
    color: #999;
}

.search-button {
    margin-left: 8px;
    padding: 6px 10px;
    background-color: #f0f0f0;
    border: none;
    border-radius: 4px;
    font-size: 14px;
    cursor: pointer;
}

.my-table {
    width: 100%;
    text-align: center;
}

.my-table th {
    background-color: #f2f2f2;
    padding: 10px;
    text-align: center;
    font-weight: bold;
}


.my-table td {
    padding: 10px;
}

.my-table tbody tr:nth-child(even) {
    background-color: #f9f9f9;
}

.my-table tbody tr:hover {
    background-color: #e5e5e5;
}
</style>
  