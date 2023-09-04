<template>
    <v-card width="100%" elevation="0">
        <div class="search-bar">
            <v-icon class="search-icon">mdi-magnify</v-icon>
            <input type="text" v-model="searchQuery" class="search-input" />
        </div>

        <table class="my-table">
            <thead>
                <tr>
                    <th v-for="header in headers" :key="header.key">{{ header.label }}</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="item in filteredData" :key="item.id">
                    <td v-for="header in headers" :key="header.key">{{ item[header.key] }}</td>
                        <v-icon class="view-icon" @click="viewUserHistory(item['username'])">mdi-eye</v-icon>  
                </tr>
            </tbody>
        </table>
    </v-card>

    <div v-if="dialogVisible">
        <v-dialog v-model="show" max-width="70%" color="black">
            <v-card style="background-color: #CBB179; border-radius: 20px;" class="card-content">
                <v-toolbar style="background-color: #0ae489; border-radius: 20px 20px 0px 0px; text-align: center;">
                    <v-toolbar-title style="font-size:x-large;">Employee History</v-toolbar-title>
                    <v-btn icon @click="closeDialog">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-toolbar>
                <EmployeeHistory :selectedId="id" />
            </v-card>
        </v-dialog>
    </div>
</template>
  
<script>
import axios from '@/plugins/axios';
import EmployeeHistory from '@/views/admin/view/EmployeeHistory.vue';

export default {
    components: {
        EmployeeHistory,
    },
    data() {
        return {
            searchQuery:'',
            show: true,
            id: '',
            dialogVisible: false,
            headers: [
                { label: 'Username', key: 'username' },
                { label: 'Name', key: 'name' },
                { label: 'email', key: 'email'},
                { label: 'Department Name', key: 'departmentName' },
            ],
            employees: null,
            history: null,
            employeesId: null,
            requests: null,
        };
    },
    async created() {
        try {
        
            const response = await axios.get('http://localhost:8080/admin/employees');
            this.employees = response.data;
        } catch (error) {
            console.error(error);
        }
    },


    computed: {
        filteredData() {
            const query = this.searchQuery.toLowerCase();
            let filteredData = this.employees;
            if (query) {
                filteredData = filteredData.filter(item =>
                    Object.values(item).some(value =>
                        value.toString().toLowerCase().includes(query)
                    )
                );
            }
            return filteredData;
        },
    },

    methods: {
        
        viewUserHistory(id) {
            this.id = id;
            this.dialogVisible = true;
        },
        closeDialog() {
            this.dialogVisible = false;
        }
    }

};


</script>

<style scoped>
.my-table ::-webkit-scrollbar {
    width: 0;
    background-color: transparent;

}

.card-content::-webkit-scrollbar {
  width: 0;
  background-color: transparent;
}

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



.search-bar {
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

.custom-row {
    margin: 0px 15px 0px 20px;
}
</style>
