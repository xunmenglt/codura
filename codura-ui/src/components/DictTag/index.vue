<template>
  <div>
    <template v-for="(item, index) in options">
      <template v-if="values.includes(item.value)">
        
        <span
          v-if="(item.listClass == 'default' || item.listClass == '') && (item.cssClass == '' || item.cssClass == null)"
          :key="item.value"
          :index="index"
          :class="item.cssClass"
          >{{ item.label + ' ' }}</span
        >
        <el-tag
          v-else
          :disable-transitions="true"
          :key="item.value"
          :index="index"
          :type="item.listClass == 'primary' ? '' : item.listClass"
          :class="item.cssClass"
        >
          {{ item.label + ' ' }}
        </el-tag>
      </template>
    </template>
    <template v-if="unmatch && showValue">
      {{ unmatchArray | handleArray }}
    </template>
  </div>
</template>

<script>
export default {
  name: "DictTag",
  props: {
    options: {
      type: Array,
      default: null,
    },
    value: [Number, String, Array],
    // 当未找到匹配的数据时，显示value
    showValue: {
      type: Boolean,
      default: true,
    },
    separator: {
      type: String,
      default: ","
    }
  },
  data() {
    return {
      unmatchArray: [], // 记录未匹配的项
    }
  },
  computed: {
    values() {
      if (this.value === null || typeof this.value === 'undefined' || this.value === '') return []
      if (Array.isArray(this.value)){
          return  this.value.map(item => '' + item)
      }
      if (typeof(this.value)=='string'){
        return this.value.split(this.separator)
      }
      return [this.value]
    },
    unmatch() {
      this.unmatchArray = []
      // 没有value不显示
      if (this.value === null || typeof this.value === 'undefined' || this.value === '' || this.options.length === 0) return false
      // 传入值为数组
      let unmatch = false // 添加一个标志来判断是否有未匹配项
      this.values.forEach(item => {
        if (!this.options.some(v => v.value === item)) {
          this.unmatchArray.push(item)
          unmatch = true // 如果有未匹配项，将标志设置为true
        }
      })
      return unmatch // 返回标志的值
    },

  },
  filters: {
    handleArray(array) {
      if (array.length === 0) return '';
      return array.reduce((pre, cur) => {
        return pre + ' ' + cur;
      })
    },
  }
};
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
