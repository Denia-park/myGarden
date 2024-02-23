<script setup>

import {computed} from "vue";

const props = defineProps({
  addTableClass: Array,
  trColor: String,
  isTotal: Boolean,
  startDate: String,
  endDate: String,
  routineData: Object
});

let tableClass = ['table', 'table-hover', 'table-bordered', ...props.addTableClass];
let stickyClass = ['sticky', props.trColor];

function aggregateActivities(activities) {
  //if activities object is empty, return
  if (Object.keys(activities).length === 0) {
    return {
      date: `${props.startDate} ~ ${props.endDate}`,
      content: {
        totalHours: 0,
        types: {}
      }
    };
  }

  const aggregated = {
    date: `${props.startDate} ~ ${props.endDate}`,
    content: {
      totalHours: 0,
      types: {}
    }
  };

  activities.forEach(activity => {
    aggregated.content.totalHours += activity.content.totalHours;

    Object.keys(activity.content.types).forEach(type => {
      if (!aggregated.content.types[type]) {
        aggregated.content.types[type] = {
          hours: 0,
          percentage: ""
        };
      }

      aggregated.content.types[type].hours += activity.content.types[type].hours;
    });
  });

  Object.keys(aggregated.content.types).forEach(type => {
    const percentage = (aggregated.content.types[type].hours / aggregated.content.totalHours) * 100;
    aggregated.content.types[type].percentage = percentage.toFixed(2) + '%';
  });

  return aggregated;
}

const tableName = computed(() => {
  if (props.isTotal) {
    return `전체 통계 : ${props.startDate} ~ ${props.endDate}`;
  } else {
    return props.routineData.date;
  }
});

function sortingTypes(types) {
  return Object.entries(types).sort((a, b) => b[1].hours - a[1].hours);
}

const content = computed(() => {
  if (props.isTotal) {
    const result = aggregateActivities(props.routineData);
    result.content.types = sortingTypes(result.content.types);

    return result.content;
  } else {
    return {
      totalHours: props.routineData.content.totalHours,
      types: sortingTypes(props.routineData.content.types)
    };
  }
});
</script>

<template>
  <table :class="tableClass">
    <thead :class="stickyClass">
    <tr>
      <th colspan="3" scope="col">{{ tableName }}</th>
    </tr>
    <tr>
      <th scope="col">타입</th>
      <th scope="col">시간</th>
      <th scope="col">비율</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(value, idx) in content.types" :key="value[0]">
      <th scope="row">{{ value[0] }}</th>
      <td>{{ value[1].hours }}</td>
      <td>{{ value[1].percentage }}</td>
    </tr>
    <tr :class="stickyClass">
      <th scope="row">Total</th>
      <td>{{ content.totalHours }}</td>
      <td>100%</td>
    </tr>
    </tbody>
  </table>
</template>

<style scoped>

h1 {
  text-align: center;
  margin: 20px 0;
}

table {
  text-align: center;

  border: 3px solid;
}

table thead tr th {
  font-weight: bold;
}

.sticky {
  position: sticky;
  top: 0;
  bottom: 0;
}

.sticky th, .sticky td {
  font-weight: bold;
}

</style>
