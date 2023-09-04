<template>
    <v-form @submit.prevent="submitForm">
        <v-container>
            <v-row>
                <v-col>
                    <v-text-field v-model="approver.username" label="Username" readonly required></v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-text-field v-model="approver.email" label="Email" readonly required></v-text-field>
                </v-col>
            </v-row>
            <v-row>
                <v-col>
                    <v-autocomplete v-model="approver.departmentId" :items="departments" label="Select department"
                        item-title="departmentName" item-value="departmentId" />
                </v-col>
                <v-col>
                    <v-autocomplete v-model="approver.userLevel" :items="[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]"
                        label="Select Approver Level" item-title="departmentName" item-value="departmentId" />
                </v-col>
            </v-row>
            <v-row class="submit-row">
                <v-col class="text-center">
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
        approver: {
            type: Array[String],
            required: true
        }
    },
    data() {
        return {
            departments: [],
            showSnackbar: false,
            snackbarColor: '',
            snackbarMessage: ''
        };
    },
    async created() {
        try {
            const response = await axios.get('http://localhost:8080/departments');
            this.departments = response.data;
        } catch (error) {
            console.error(error);
        }
    },

    methods: {
        async submitForm() {
            try {
                await axios.put('http://localhost:8080/admin/approver', {
                    userId: this.approver.userId,
                    departmentId: this.approver.departmentId,
                    userLevel: this.approver.userLevel
                });
                
                this.showSuccessSnackbar('Approver updated successfully!');
            } catch (error) {
                console.error(error);
                this.showErrorSnackbar(error);
            }
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
    margin: 0px auto 0px auto;
}

.submit-row {
    display: flex;
    justify-content: center;
}
</style>
  