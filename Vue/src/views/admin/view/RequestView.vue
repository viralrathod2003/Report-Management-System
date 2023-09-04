<template>
  <v-card width="100%" elevation="0">


    <!-- filter custom-dropdown -->

    <div class="filter-section">
      <v-icon class="filter-icon">mdi-filter</v-icon>
      <label for="statusSelect">Status:</label>
      <select class="custom-dropdown" v-model="selectedStatus" @change="performSearch">
        <option value="">All</option>
        <option v-for="status in statuses" :value="status" :key="status">
          <div v-if="status === 100">Rejected</div>
          <div v-else-if="status === 200">Approved</div>
          <div v-else>{{ status }}</div>
        </option>
      </select>
    </div>

    <!-- search-bar -->

    <div class="search-bar">
      <v-icon class="search-icon">mdi-magnify</v-icon>
      <input type="text" v-model="searchQuery" placeholder="Search" class="search-input" />
    </div>

    <table class="my-table">
      <thead>
        <tr>
          <th v-for="header in requestHeaders" :key="header.key">{{ header.label }}</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in filteredData" :key="item.requestId">
          <td v-for="header in requestHeaders" :key="header.key">{{ item[header.key] }}</td>
          <v-icon class="view-icon" @click="viewRequest(item['requestId'])">mdi-eye</v-icon>
          <v-icon class="action-icon" @click="editRequest(item)">mdi-pencil</v-icon>
        </tr>
      </tbody>
    </table>

    <div v-if="val === 1">
      <v-dialog v-model="show" max-width="600px" color="black">
        <v-card style="background-color: #CBB179; border-radius: 20px;" class="card-content">
          <v-toolbar style="background-color: #0ae489; border-radius: 20px 20px 0px 0px; text-align: center;">
            <v-toolbar-title style="font-size:x-large;">Request Details</v-toolbar-title>
            <v-btn icon @click="closeDialog">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
          <StepVerification :selectedId="id" />
        </v-card>
      </v-dialog>
    </div>

    <div v-if="val === 2">
      <v-dialog v-model="show" max-width="600px" color="black" >
        <v-card style="border-radius: 20px;" class="card-content">
          <v-toolbar style="background-color: #0ae489; border-radius: 20px 20px 0px 0px; text-align: center;">
            <v-toolbar-title style="font-size:x-large;">Request Details</v-toolbar-title>
            <v-btn icon @click="closeDialog">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
          <VarifyRequest :request="item" />
        </v-card>
      </v-dialog>
    </div>
  </v-card>
</template>

<script>
import axios from '@/plugins/axios';
import VarifyRequest from '@/views/admin/modify/VarifyRequest.vue';
import StepVerification from '@/components/StepVerification.vue';

export default {
  components: {
    StepVerification,
    VarifyRequest,
  },
  data() {
    return {
      show: true,
      searchQuery: '',
      selectedStatus: '',
      item: '',
      id: '',
      val: 0,
      requestHeaders: [
        { label: 'Request ID', key: 'requestId' },
        { label: 'Employee name', key: 'username' },
        { label: 'Report Name', key: 'reportName' },
        { label: 'Request Date', key: 'requestDate' },
        { label: 'Request Description', key: 'requestDescription' },
        { label: 'Request Status', key: 'requestStatus' },
      ],
      requestData: null,
    };
  },
  async created() {
    try {
      const response = await axios.get('http://localhost:8080/admin/requests');
      this.requestData = response.data;
    } catch (error) {
      console.error(error);
    }
  },

  computed: {
    filteredData() {
      const selectedStatus = this.selectedStatus;
      const query = this.searchQuery.toLowerCase();

      let filteredData = this.requestData;

      if (selectedStatus || selectedStatus === 0)
        filteredData = filteredData.filter(item => item.requestStatus === selectedStatus);

      if (query) {
        filteredData = filteredData.filter(item =>
          Object.values(item).some(value =>
            value.toString().toLowerCase().includes(query)
          )
        );
      }
      return filteredData;
    },

    statuses() {
      let uniqueStatuses = '';
      if (this.requestData)
        uniqueStatuses = [...new Set(this.requestData.map(item => item.requestStatus))];
      return uniqueStatuses;
    },

  },

  methods: {
    editRequest(item) {
      this.item = item;
      this.val = 2;
    },
    viewRequest(id) {
      this.id = id;
      this.val = 1;
    },
    closeDialog() {
      this.val = 0;
    }
  }
};
</script>


<style scoped>

.card-content::-webkit-scrollbar {
  width: 0;
  background-color: transparent;
}
.custom-dropdown {
  padding: 5px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
  background-color: #bae5dd;
  color: #333;
  cursor: pointer;
  min-width: 200px;
  margin: 10px;
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
  