<template>
      <JsonTable :headers="headers" :table-data="departments" />
  </template>
    
  <script>
  import JsonTable from '@/components/JsonTable.vue';
  import axios from '@/plugins/axios';
  
  export default {
    components: {
      JsonTable,
    },
    data() {
      return {
        departments: null,
        headers: [
          { label: 'Department Id', key: 'departmentId' },
          { label: 'Department Name', key: 'departmentName' },
        ],
        allApprovers: [],
        info: null,
      };
    },

    async created() {
      try {
        const response = await axios.get('http://localhost:8080/departments');
        this.departments = response.data;
      } catch (error) {
        console.error(error);
      }
      console.log(this.departments)
    },
  };
  </script>
  