import { marked } from "marked";
import hljs from "highlight.js";
import "highlight.js/styles/atom-one-dark.css";

const tmp_str = `
**冒泡排序** 是一种简单的排序算法...

\`\`\`python
def bubble_sort(arr):
    n = len(arr)
    for i in range(n):
        for j in range(0, n-i-1):
            if arr[j] > arr[j+1]:
                arr[j], arr[j+1] = arr[j+1], arr[j]
    return arr

arr = [64, 34, 25, 12, 22, 11, 90]
sorted_arr = bubble_sort(arr)
print("Sorted array is:", sorted_arr)
\`\`\`

这段代码定义了一个 bubble_sort 函数...
`;

export default {
  data() {
    return {
      firstUpdate: true,
      generating: false,
      inputText: "",
      conversation: {
        id: "",
        title: "",
        messages: [
          { role: "user", content: tmp_str },
          { role: "assistant", content: tmp_str }
        ],
      },
      completion: {
        role: "",
        content: "",
        type: "",
        language: {},
        error: "",
      },
      isComposing: false,
    };
  },
  computed: {
    visibleMessages() {
      const msgs = this.conversation.messages;
      return msgs.slice(-50); // 仅展示最新50条消息
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.highlightAndEnhanceCode();
    });
  },
  methods: {
    convert2Markdown(content) {
      return content ? marked(content) : "";
    },
    scrollToBottom() {
      const container = document.getElementById("conversation-container");
      if (container) {
        container.scroll({
          top: container.scrollHeight,
          behavior: "smooth"
        });
      }
    },
    onCompositionStart() {
      this.isComposing = true;
    },
    onCompositionEnd() {
      this.isComposing = false;
    },
    onKeyUpSend(event) {
      if (this.isComposing) return;
      if (event.ctrlKey && event.code === "Enter") {
        this.inputText += "\n";
        event.preventDefault();
      } else if (!event.ctrlKey && event.code === "Enter") {
        this.handleSubmitForm();
        event.preventDefault();
      }
    },
    highlightAndEnhanceCode() {
      const blocks = this.$el.querySelectorAll("pre code:not([data-hljs])");
      blocks.forEach((block) => {
        hljs.highlightElement(block);
        block.setAttribute("data-hljs", "true");
      });
      this.addCopyButtons();
    },
    addCopyButtons() {
      const codeItems = this.$el.querySelectorAll("pre code:not([data-handled])");
      codeItems.forEach((codeItem) => {
        const parent = codeItem.parentElement;
        if (!parent || parent.tagName.toLowerCase() !== "pre") return;

        // 清理旧按钮
        const oldBtns = parent.querySelectorAll(".copy-button");
        oldBtns.forEach(btn => btn.remove());

        const lang = [...codeItem.classList].find(c => c.startsWith("language-"))?.split("language-")[1] || "";
        const wrapper = document.createElement("div");
        wrapper.className = "newdiv";

        const langTag = document.createElement("span");
        langTag.className = "codelanguage";
        langTag.textContent = lang;

        const copyBtn = document.createElement("div");
        copyBtn.className = "copy-button icon el-icon-document-copy";
        copyBtn.addEventListener("click", () => {
          const textToCopy = codeItem.textContent;
          navigator.clipboard?.writeText(textToCopy).then(() => {
            copyBtn.className = "copy-button icon el-icon-check";
            setTimeout(() => {
              copyBtn.className = "copy-button icon el-icon-document-copy";
            }, 2000);
          }).catch(err => console.error("复制失败:", err));
        });

        parent.style.position = "relative";
        parent.parentNode.insertBefore(wrapper, parent);
        wrapper.appendChild(langTag);
        wrapper.appendChild(copyBtn);
        codeItem.setAttribute("data-handled", "true");
      });
    },
    handleSubmitForm() {
      // 自定义发送逻辑...
      this.scrollToBottom();
      this.$nextTick(() => {
        this.highlightAndEnhanceCode();
      });
    }
  },
  watch: {
    generating(value) {
      if (!value) {
        this.$nextTick(() => this.highlightAndEnhanceCode());
      }
    }
  },
  updated() {
    if (this.firstUpdate) {
      this.highlightAndEnhanceCode();
      this.firstUpdate = false;
    }
  }
};
