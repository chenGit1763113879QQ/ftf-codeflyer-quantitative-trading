import {createRouter, createWebHistory} from 'vue-router'
import Layout from "@/layout/Layout";

const routes = [
    {
        path: '/',
        name: 'Layout',
        component: Layout,
        redirect: "/stock-pool",
        children: [
            {
                path: 'stock-pool',
                name: 'StockPool',
                component: () => import("@/views/StockPool"),
            },
            {
                path: 'trade-decision',
                name: 'TradeDecision',
                component: () => import("@/views/TradeDecision"),
            },
            {
                path: 'trade-decision-today',
                name: 'TradeDecisionToday',
                component: () => import("@/views/TodayTradeDecision"),
            },
            {
                path: 'decision-result',
                name: 'decisionResult',
                component: () => import("@/views/DecisionResult"),
            }
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import("@/views/Login")
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})


router.beforeEach((to, from, next) => {
    if (to.path === '/login' || to.path === '/register') {
        next()
        return
    }
    let user = sessionStorage.getItem("user") ? JSON.parse(sessionStorage.getItem("user")) : {}
    if (!user) {
        next('/login')
    } else {
        next()
    }
})

export default router
