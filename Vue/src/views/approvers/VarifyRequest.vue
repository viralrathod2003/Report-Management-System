<template>
  <v-form @submit.prevent="submitForm">
    <v-container>
      <v-row>
        <v-col cols="4">
          <v-text-field v-model="request.requestId" label="Request Date" readonly required></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-text-field v-model="request.userId" label="Request Date" readonly required></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-text-field v-model="request.requestStatus" label="Request Status" readonly required></v-text-field>
        </v-col>

        <v-col cols="6">
          <v-text-field v-model="request.requestDate" label="Request Date" readonly required></v-text-field>
        </v-col>

        <v-col cols="6">
          <v-select v-model="validation" label="Validate Request" :items="['Approve', 'Reject']"></v-select>
        </v-col>
      </v-row>
      <v-col>
        <v-textarea v-model="request.requestDescription" label="Request Description" readonly required></v-textarea>
      </v-col>
      <v-row>
        <v-col cols="12">
          <v-textarea v-model="justification" label="Justification" required></v-textarea>
        </v-col>
      </v-row>
      <v-row class="submit-row">
        <v-col cols="12" class="text-center">
          <v-btn type="submit" color="primary" class="submit-button">Submit</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-form>
  <v-snackbar v-model="showSnackbar" :color="snackbarColor" top right class="snackbar-custom">
    <span class="snackbar-message">{{ snackbarMessage }}</span>
  </v-snackbar>
</template>
  
<script>
import axios from '@/plugins/axios';

export default {
  props: {
    request: {
      type: Array[String],
      required: true
    }
  },
  data() {
    return {
      approvalStatus: null,
      actionTaken: '',
      justification: '',
      validation: '',
      showSnackbar: false,
      snackbarColor: '',
      snackbarMessage: ''
    };
  },

  methods: {
    async submitForm() {
      try {
        const payload = {
          requestId: this.request.requestId,
          actionDescription: this.justification,
          actionTaken: this.validation === 'Approve'
        };

        await axios.put('http://localhost:8080/approver/request', payload);

        this.showSuccessSnackbar('Request action submitted successfully!');
        this.resetForm();
      } catch (error) {
        console.error(error);
        this.showErrorSnackbar('An error occurred while submitting the request action.');
      }
    },

    resetForm() {
      this.validation = '';
      this.justification = '';
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
  
<style scoped>
.submit-button {
  display: block;
  margin: 0 auto;
}

.submit-row {
  display: flex;
  justify-content: center;
}
</style>
  