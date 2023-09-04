
<template>
  <div>
    <v-card-text v-if="message">
      <!-- report Description  -->
      <div class="requested_by" style="text-align: center; margin-bottom: 50px;">
        <p>requested by: <b>{{ request.username }}</b></p>
        <p>Report Name: <b>{{ report.reportName }}</b></p>
        <p>Report Department <b>{{ report.departmentName }}</b></p>
        <p>Request Des: <b>{{ request.requestDescription }}</b></p>
      </div>

      <v-timeline align="start" truncate-line="both" class="timeline" >
        <v-timeline-item dot-color="#CCCCCC" size="small">
          <strong>Request initiated : {{ request.requestDate }} </strong>
        </v-timeline-item>
        <v-timeline-item v-for="action in actions" :key="action.actionId" :dot-color="getDotColor(action)" size="small">
          <div class="mb-5">
            <p v-if="action.actionTaken">Status : <b>Approved</b></p>
            <p v-if="!action.actionTaken">Status : <b>Rejected</b></p>
            <p>Verified by : <b> {{ action.username }} </b></p>
            <p>Verified on : <b> {{ action.actionDate }}</b></p>
            <br>
            <strong>Action description</strong>
            <p>{{ action.actionDescription }}</p>
          </div>
        </v-timeline-item>

        <!-- actions required -->
        <v-timeline-item v-for="levels in approvers" :key="levels[0].userLevel" dot-color="#0000FF" size="small">
          <div class="mb-5">
            <div class="font-weight-normal">
              <strong>pending {{ levels[0].userLevel }}</strong>
            </div>
            <div v-for="approver in levels" style="display: inline;">
              {{ approver.username + " " }}
            </div>
          </div>
        </v-timeline-item>
      </v-timeline>
    </v-card-text>
  </div>
</template>

<script>
import axios from '@/plugins/axios';

export default {
  props: {
    selectedId: {
      type: String,
      required: true,
    },
  },

  data() {
    return {
      message: null,
      actions: null,
      approvers: null,
      employee: null,
      report: null,
      request: null,
      searchQuery: '',
    };
  },

  async created() {
    this.message = false;
    this.actions = null;
    this.approvers = null;
    this.employee = null;
    this.report = null;
    this.request = null;
    try {
      const response = await axios.get(`http://localhost:8080/request/details/${this.selectedId}`);
      this.actions = response.data.actions;
      this.approvers = response.data.approversByLevel;
      this.report = response.data.report;
      this.request = response.data.request;
      this.message = true;
    } catch (error) {
      console.error(error);
    }
  },

  methods: {
    getDotColor(action) {
      return action.actionTaken ? 'green' : 'red';
    },
  },
};
</script>
