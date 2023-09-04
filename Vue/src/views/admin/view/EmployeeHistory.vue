<template>
    <v-card width="100%" elevation="0">
        <JsonTable :headers="headers" :table-data="actions" />
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
            headers: [
                { label: 'Request Id', key: 'requestId' },
                { label: 'Report Id', key: 'reportId' },
                { label: 'Request Description', key: 'requestDescription' },
                { label: 'Request Status', key: 'requestStatus' },
            ],

            approvers: null,
            history: null,
            approverId: null,
            actions: null,


        };
    },

    async created() {
        try {
            const response = await axios.get(`http://localhost:8080/admin/employee/history/${this.selectedId}`)
            this.actions = response.data;
        } catch (error) {
            console.error(error);
        }
    },

};
</script>
  