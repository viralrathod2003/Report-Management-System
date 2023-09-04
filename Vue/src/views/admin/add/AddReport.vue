<template>
    <v-container>
        <v-card class="custom-card" elevation="0">
            <v-card-text>
                <v-form @submit.prevent="submitForm">
                    <v-row>
                        <v-col>
                            <v-text-field variant="solo" placeholder="Report Name" v-model="reportName"
                                bg-color="#DDE6ED"></v-text-field>
                        </v-col>
                        <v-col>
                            <v-text-field variant="solo" placeholder="Report Path" v-model="reportPath"
                                bg-color="#DDE6ED"></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row justify="center">
                        <v-col cols="12" sm="6">
                            <v-autocomplete v-model="departmentId" :items="department" label="Select department"
                                bg-color="#DDE6ED" item-title="departmentName" item-value="departmentId" />
                        </v-col>
                    </v-row>
                    <v-row justify="center">
                        <v-col cols="12" sm="5" md="4" lg="3">
                            <v-btn type="submit" color="#890F0D" block
                                style="color: white; border-radius: 10px;">Submit</v-btn>
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
            departmentId: null,
            reportName: null,
            reportPath: null,
            showSnackbar: false,
            snackbarColor: '',
            snackbarMessage: ''
        };
    },

    methods: {
        async submitForm() {
            try {
                await axios.post('http://localhost:8080/admin/report', {
                    departmentId: this.departmentId,
                    reportName: this.reportName,
                    reportPath: this.reportPath
                });

                this.showSuccessSnackbar('Report added successfully!');
                this.resetForm();
            } catch (error) {
                console.error(error);
                this.showErrorSnackbar('An error occurred while adding the report.');
            }
        },

        resetForm() {
            this.departmentId = null;
            this.reportName = null;
            this.reportPath = null;
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
  