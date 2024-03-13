import './assets/main.css'

import {createApp} from 'vue'
import App from './App.vue'
import {store} from "@/scripts/store.js";
import {router, routerBeforeEach} from "@/scripts/router.js";
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

/** Vcalendar : 달력 라이브러리 */
import VCalendar from 'v-calendar';
import 'v-calendar/style.css';

// VueCalendarHeatmap (GitHub 잔디)
import VueCalendarHeatmap from 'vue3-calendar-heatmap'

// Prism
import Prism from 'prismjs';
// highlight code
import 'prismjs/components/prism-json';
import createCopyCodePlugin from "@kangc/v-md-editor/lib/plugins/copy-code/index.js";
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
import createTodoListPlugin from "@kangc/v-md-editor/lib/plugins/todo-list/index.js";
import '@kangc/v-md-editor/lib/plugins/todo-list/todo-list.css';
import createLineNumbertPlugin from "@kangc/v-md-editor/lib/plugins/line-number/index.js";
import koKr from "@kangc/v-md-editor/es/lang/ko-KR.js";

// Markdown Editor
VMdEditor.use(vuepressTheme, {
    Prism,
    config: {
        toc: {
            includeLevel: [1, 2, 3, 4]
        }
    }
}).use(createCopyCodePlugin())
    .use(createTodoListPlugin())
    .use(createLineNumbertPlugin())
    .lang.use('ko-KR', koKr)

// Router 이동 전에 실행되는 함수
routerBeforeEach(router);

createApp(App).use(store).use(router).use(VMdEditor).use(VueCalendarHeatmap).use(VCalendar, {}).mount('#app')
