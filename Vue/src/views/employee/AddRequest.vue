<template>
    <v-container>
      <v-card class="custom-card" elevation="0">
        <v-card-text>
          <v-form @submit.prevent="submitForm">
            <v-row justify="center">
              <v-col cols="12" sm="6">
                <v-autocomplete v-model="departmentId" :items="departments" label="Select department"
                  bg-color="#DDE6ED" item-title="departmentName" item-value="departmentId" />
              </v-col>
              <v-col cols="12" sm="6">
                <v-autocomplete v-model="reportId" :items="departmentMap[departmentId]" label="Select a report" bg-color="#DDE6ED"
                  item-title="reportName" item-value="reportId"></v-autocomplete>
              </v-col>
            </v-row>
            <v-row justify="center">
              <v-col cols="12">
                <v-textarea v-model="description" label="Description for your request" multi-line
                  class="custom-textarea" bg-color="#DDE6ED"></v-textarea>
              </v-col>
            </v-row>
            <v-row justify="center">
              <v-col cols="12" sm="5" md="4" lg="3">
                <v-btn type="submit" color="#890F0D" block style="color: white; border-radius: 10px;">Submit</v-btn>
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
      </v-card>
  
      <v-snackbar v-model="showSnackbar" :color="snackbarColor" top right class="snackbar-custom">
        <span class="snackbar-message">{{ snackbarMessage }}</span>
      </v-snackbar>
    </v-container>
  </template>
  
  <script>
  import axios from '@/plugins/axios';
  
  export default {
    data() {
      return {
        description: '',
        departments: [],
        departmentId: null,
        reports: [],
        reportId: null,
        departmentMap: {},
        showSnackbar: false,
        snackbarColor: '',
        snackbarMessage: ''
      };
    },
  
    methods: {
      async submitForm() {
        try {
          const payload = {
            reportId: this.reportId,
            requestDescription: this.description
          };
  
          await axios.post('http://localhost:8080/user/request', payload);
          this.showSuccessSnackbar('Request added successfully!');
          this.resetForm();
        } catch (error) {
          console.error(error);
          this.showErrorSnackbar('An error occurred while submitting the form.');
        }
      },
  
      resetForm() {
        this.description = '';
        this.departmentId = null;
        this.reportId = null;
      },
  
      showSuccessSnackbar(message) {
        this.showSnackbar = true;
        this.snackbarColor = 'success';
        this.snackbarMessage = message;
      },
  
      showErrorSnackbar(message) {
        this.showSnackbar = true;
        this.snackbarColor = 'error';
        this.snackbarMessage = message;
      }
    },
  
    async created() {
      try {
        const [departmentsResponse, reportsResponse] = await Promise.all([
          axios.get('http://localhost:8080/departments'),
          axios.get('http://localhost:8080/reports')
        ]);
  
        this.departments = departmentsResponse.data;
        this.reports = reportsResponse.data;
  
        this.departmentMap = this.reports.reduce((map, report) => {
          if (!map[report.departmentId]) {
            map[report.departmentId] = [];
          }
          map[report.departmentId].push(report);
          return map;
        }, {});
      } catch (error) {
        console.error(error);
      }
    }
  };
  </script>
  