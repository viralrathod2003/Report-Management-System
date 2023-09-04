<template>
    <v-container>
      <v-card class="custom-card" elevation="0">
        <v-card-text>
          <v-form @submit.prevent="submitForm">
            <v-row>
              <v-col>
                <v-text-field variant="solo" placeholder="Approver full name" v-model="departmentName" bg-color="#DDE6ED"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea v-model="departmentDecription" label="Description for your request" multi-line class="custom-textarea"></v-textarea>
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
        departmentName: null,
        departmentDecription: null,
        showSnackbar: false,
        snackbarColor: '',
        snackbarMessage: ''
      };
    },
  
    methods: {
      async submitForm() {
        try {
          await axios.post('http://localhost:8080/admin/department', {
            departmentName: this.departmentName,
            departmentDecription: this.departmentDecription
          });
  
          this.showSuccessSnackbar('Department added successfully!');
          this.resetForm();
        } catch (error) {
          console.error(error);
          this.showErrorSnackbar('An error occurred while adding the department.');
        }
      },
  
      resetForm() {
        this.departmentName = null;
        this.departmentDecription = null;
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
    }
  };
  </script>
  
  <style>
  .snackbar-custom {
    top: 20px;
    right: 20px;
  }
  
  .snackbar-message {
    color: white;
    font-weight: bold;
  }
  </style>
  