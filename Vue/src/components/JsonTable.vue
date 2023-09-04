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
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in filteredTableData" :key="item.id">
          <td v-for="header in headers" :key="header.key">{{ item[header.key] }}</td>
        </tr>
      </tbody>
    </table>
  </v-card>
</template>

<script>
import { defineComponent } from 'vue';

export default defineComponent(

  {
    props: {
      headers: {
        type: [Array, String],
        required: true,
      },
      tableData: {
        type: [Array, String],
        required: true,
      },
    },
    data() {
      return {
        searchQuery: '',
      };
    },
    computed: {
      filteredTableData() {
        if (!this.searchQuery) {
          return this.tableData;
        } else {
          const query = this.searchQuery.toLowerCase();
          return this.tableData.filter(item => {
            return Object.values(item).some(value => {
              return value.toString().toLowerCase().includes(query);
            });
          });
        }
      },
    },
  });
</script>

<style scoped>
.my-table ::-webkit-scrollbar {
  width: 0;
  background-color: transparent;
  margin-bottom: 50px;

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
