const programmer = {
  state: {
    token:'',
    id: '',
    name: '',
    avatar: '',
    roles: [],
    permissions: []
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_ID: (state, id) => {
      state.id = id
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    }
  },

  actions: {
    // 刷新用户信息
    FlashUserInfo({commit},userInfo){
      const name = userInfo.username.trim()
      const token=userInfo.token
      const id=userInfo.id
      const avatar=userInfo.avatar
      const roles=userInfo.roles
      const permissions=userInfo.permissions
      return new Promise(resolve => {
        commit('SET_TOKEN', token)
        commit('SET_ID', id)
        commit('SET_NAME', name)
        commit('SET_AVATAR', avatar)
        commit('SET_ROLES', roles)
        commit('SET_PERMISSIONS', permissions)
        resolve()
      })
    },
    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        commit('SET_ID', '')
        commit('SET_NAME', '')
        commit('SET_AVATAR', '')
        commit('SET_ROLES', [])
        commit('SET_PERMISSIONS', [])
        resolve()
      })
    }
  }
}

export default programmer
