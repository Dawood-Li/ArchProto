import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: () => import('./views/Home.vue')
  },
  {
    path: '/login',
    component: () => import('./views/Login.vue')
  },
  {
    path: '/register',
    component: () => import('./views/Register.vue')
  },
  {
    path: '/home',
    component: () => import('./views/Home.vue')
  },
  {
    path: '/note',
    component: () => import('./views/Note.vue')
  }, 
  {
    path: '/noteView/:dataId',
    component: () => import('./views/NoteView.vue'),
    props: true
  },
  {
    path: '/chat',
    component: () => import('./views/Chat.vue')
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router