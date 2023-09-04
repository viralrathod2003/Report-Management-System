<template>
    <v-card width="100%" elevation="0">
        <JsonTable :headers="actions_headers" :table-data="actions" />
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
            actions_headers: [
                { label: 'Action Id', key: 'actionId' },
                { label: 'Action Date', key: 'actionDate' },
                { label: 'Action Description', key: 'actionDescription' },
                { label: 'Action Level', key: 'actionLevel' },
                { label: 'Action Taken', key: 'actionTaken' },
            ],

            approvers: null,
            history: null,
            approverId: null,
            actions: null,


        };
    },

    async created() {
        try {
            const response = await axios.get(`http://localhost:8080/admin/approver/history/${this.selectedId}`)
            this.actions = response.data;
        } catch (error) {
            console.error(error);
        }
    },

};
</script>
  