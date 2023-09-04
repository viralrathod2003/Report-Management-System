<template>
    <v-container>
      <v-card class="custom-card" elevation="0">
        <v-card-text>
          <v-form @submit.prevent="submitForm">
            <v-col>
              <v-text-field variant="solo" placeholder="Approver full name" v-model="name" bg-color="#DDE6ED"></v-text-field>
            </v-col>
            <v-col>
              <v-text-field variant="solo" placeholder="Username" v-model="username" bg-color="#DDE6ED"></v-text-field>
            </v-col>
            <v-col>
              <v-text-field variant="solo" placeholder="email" v-model="email" bg-color="#DDE6ED"></v-text-field>
            </v-col>
            <v-col>
              <v-text-field variant="solo" placeholder="Approver Level" v-model="userLevel" bg-color="#DDE6ED"></v-text-field>
            </v-col>
  
            <v-row justify="center">
              <v-col cols="12" sm="6">
                <v-autocomplete v-model="departmentId" :items="department" label="Select department" bg-color="#DDE6ED"
                  item-title="departmentName" item-value="departmentId" />
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
        department: [],
        username: null,
        name: null,
        email: null,
        password: null,
        userLevel: null,
        departmentId: null,
        showSnackbar: false,
        snackbarColor: '',
        snackbarMessage: ''
      };
    },
  
    methods: {
      async submitForm() {
        try {
          await axios.post('http://localhost:8080/admin/approver', {
            username: this.username,
            name: this.name,
            password: this.username,
            userLevel: this.userLevel,
            email: this.email,
            departmentId: this.departmentId,
            enabled: 1
          });
  
          this.showSuccessSnackbar('Approver added successfully!');
          this.resetForm();
        } catch (error) {
          console.error(error);
          this.showErrorSnackbar('An error occurred while adding the approver.');
        }
      },
  
      resetForm() {
        this.username = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.userLevel = null;
        this.departmentId = null;
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
        const response = await axios.get('http://localhost:8080/departments');
        this.department = response.data;
      } catch (error) {
        console.error(error);
      }
    }
  };
  </script>
  