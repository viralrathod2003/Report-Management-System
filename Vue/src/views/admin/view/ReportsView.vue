<template>
  <v-card width="100%" class="overflow-auto table" elevation="0">
    <div class="search-bar">
      <v-icon class="search-icon">mdi-magnify</v-icon>
      <input type="text" v-model="searchQuery" placeholder="Search" class="search-input" @keyup.enter="performSearch" />
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
        <tr v-for="item in filteredTableData" :key="item.id">
          <td v-for="header in headers" :key="header.key">{{ item[header.key] }}</td>
          <v-icon class="view-icon" @click="viewAccessList(item['reportId'])">mdi-eye</v-icon>
        </tr>
      </tbody>
    </table>
  </v-card>
  <div v-if="dialogVisible">
    <v-dialog v-model="show" max-width="70%" color="black">
      <v-card style="background-color: #CBB179; border-radius: 20px;" class="card-content">
        <v-toolbar style="background-color: #0ae489; border-radius: 20px 20px 0px 0px; text-align: center;">
          <v-toolbar-title style="font-size:x-large;">Employee List</v-toolbar-title>
          <v-btn icon @click="closeDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-toolbar>
        <AccessList :selectedId="id" />
      </v-card>
    </v-dialog>
  </div>
</template>
  
<script>
import axios from '@/plugins/axios';
import AccessList from '@/views/admin/view/AccessList.vue';


export default {
  components: {
    AccessList,
  },
  data() {
    return {
      searchQuery: '',
      show:true,
      dialogVisible: false,
      reports: null,
      headers: [
        { label: 'Report Id', key: 'reportId' },
        { label: 'Report Name', key: 'reportName' },
        { label: 'Report path', key: 'reportPath' },
        { label: 'Report Department', key: 'departmentName' },
      ],
      allReports: [],
      info: null,
    };
  },

  computed: {
    filteredTableData() {
      if (!this.searchQuery) {
        return this.reports;
      } else {
        const query = this.searchQuery.toLowerCase();
        return this.reports.filter(item => {
          return Object.values(item).some(value => {
            return value.toString().toLowerCase().includes(query);
          });
        });
      }
    },
  },

  async created() {
    try {
      const response = await axios.get('http://localhost:8080/reports');
      this.reports = response.data;
    } catch (error) {
      console.error(error);
    }
    console.log(this.reports)
  },
  methods: {
    viewAccessList(id) {
      this.id = id;
      this.dialogVisible = true;
    },
    closeDialog() {
      this.dialogVisible = false;
    }
  },
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
