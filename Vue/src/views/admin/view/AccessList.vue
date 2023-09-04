<template>
    <v-card width="100%" elevation="0">
        <JsonTable :headers="users" :table-data="actions" />
    </v-card>
</template>
  
<script>
import JsonTable from '@/components/JsonTable.vue';
import axios from '@/plugins/axios';

export default {
    props: {
        selectedId: {
            type: String,
            required: true,
        },
    },
    components: {
        JsonTable,
    },
    data() {
        return {
            users: [
                { label: 'Username', key: 'username' },
                { label: 'Name', key: 'name' },
                { label: 'Email', key: 'email' },
                { label: 'Department', key: 'departmentName' },
            ],

            approvers: null,
            history: null,
            approverId: null,
            actions: null,


        };
    },

    async created() {
        try {
            const response = await axios.get(`http://localhost:8080/admin/report/hasAccess/${this.selectedId}`)
            this.actions = response.data;
        } catch (error) {
            console.error(error);
        }
    },

};
</script>
  