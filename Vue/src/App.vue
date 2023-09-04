<template>
  <RouterView />
</template>
<script setup>
import { RouterView } from 'vue-router';
</script>




<!-- <template>
  <div>
    <h1>Approval Timeline</h1>
    {{ selectedDepartment }}
    {{ filterApprovers }}
    <label for="departmentSelect">Department:</label>
    <select class="custom-dropdown" v-model="selectedDepartment">
      <option value="">All</option>
      <option v-for="department in departments" :value="department.departmentId" :key="department.departmentId">
        {{ department.departmentName }}
      </option>
    </select>

    <v-timeline align="start">
      <v-timeline-item v-for="levelApprovers in filteredApprovers" :key="levelApprovers.level" dot-color="green" size="small">
        <template v-slot:opposite>
          <div :class="`pt-1 headline font-weight-bold text-green`"></div>
        </template>
        <div>
          <h2 :class="`mt-n1 headline font-weight-light mb-4 text-red`">
            Level : {{ levelApprovers.userLevel }}
          </h2>
          <div v-for="approver in levelApprovers.approvers" :key="approver.userId">
             Name : {{ approver.username }}
          </div>
        </div>
      </v-timeline-item>
    </v-timeline>
  </div>
</template>

<script>
export default {
  data() {
    return {
      departments: [],
      selectedDepartment: '',
      departmentMap: new Map(),
    };
  },
  computed: {
    filteredApprovers() {
      if (this.selectedDepartment === '') {
        return Array.from(this.departmentMap.values()).flatMap(department => department);
      } else {
        return this.departmentMap.get(this.selectedDepartment) || [];
      }
    },
  },
  created() {
    this.generateData();
  },
  methods: {
    generateData() {
      const approvers = [
        {
          "userId": 2,
          "username": "a",
          "name": "a",
          "userLevel": 2,
          "email": "john.doe@exaaddjasnkd@gmail.com",
          "departmentId": 1,
          "departmentName": "Sales"
        },
        {
          "userId": 3,
          "username": "demo",
          "name": "demo",
          "userLevel": 2,
          "email": "demo",
          "departmentId": 2,
          "departmentName": "HR"
        },
        {
          "userId": 4,
          "username": "demo2",
          "name": "demo2",
          "userLevel": 3,
          "email": "demo2@gmail.com",
          "departmentId": 2,
          "departmentName": "HR"
        },
        {
          "userId": 6,
          "username": "demo3",
          "name": "demo3",
          "userLevel": 4,
          "email": "demo3@gmail.com",
          "departmentId": 2,
          "departmentName": "HR"
        },
        {
          "userId": 8,
          "username": "demo4",
          "name": "demo4",
          "userLevel": 5,
          "email": "demo4@gmail.com",
          "departmentId": 2,
          "departmentName": "HR"
        },
        {
          "userId": 11,
          "username": "test100",
          "name": "Test100",
          "userLevel": 2,
          "email": "testd@ga",
          "departmentId": 6,
          "departmentName": "IT"
        },
        {
          "userId": 15,
          "username": "Arvind1.Tomar",
          "name": "Arvind Tomar",
          "userLevel": 1,
          "email": "arvind@gmail.com",
          "departmentId": 2,
          "departmentName": "HR"
        },
        {
          "userId": 17,
          "username": "Arvind",
          "name": "Arvind Tomar",
          "userLevel": 1,
          "email": "aad@gmail.com",
          "departmentId": 1,
          "departmentName": "Sales"
        }
      ];

      approvers.forEach(approver => {
        if (this.departmentMap.has(approver.departmentId)) {
          const department = this.departmentMap.get(approver.departmentId);
          if (department.has(approver.userLevel)) {
            department.get(approver.userLevel).push(approver);
          } else {
            department.set(approver.userLevel, [approver]);
          }
        } else {
          const department = new Map();
          department.set(approver.userLevel, [approver]);
          this.departmentMap.set(approver.departmentId, department);
        }
      });

      this.departments = Array.from(this.departmentMap.entries()).map(([departmentId, levelMap]) => {
        const approvers = Array.from(levelMap.entries()).map(([level, approverList]) => {
          return {
            level,
            approvers: approverList
          };
        });

        const departmentName = approvers[0].approvers[0].departmentName;

        return {
          departmentId,
          departmentName,
          approvers
        };
      });
    },
  },
};
</script> -->
