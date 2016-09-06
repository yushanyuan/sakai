/*************************************************************
 *
 *  MathJax/config/default.js
 */

MathJax.Hub.Config({ 
  jax: ["input/TeX","output/HTML-CSS"],
  extensions: ["tex2jax.js"],
  tex2jax: {
    inlineMath: [ ['$','$'], ["\\(","\\)"] ],
    displayMath: [ ['$$','$$'], ["\\[","\\]"] ],
    processEscapes: true
  },
  "HTML-CSS": { availableFonts: ["TeX"] },

  showProcessingMessages: false,
  displayAlign: "left",
  displayIndent: "0em",
  showMathMenu: false,
  showMathMenuMSIE: false,

  menuSettings: {
    zoom: "Hover",        //  when to do MathZoom
    CTRL: false,         //    require CTRL for MathZoom?
    ALT: false,          //    require Alt or Option?
    CMD: false,          //    require CMD?
    Shift: false,        //    require Shift?
    zscale: "200%",      //  the scaling factor for MathZoom
    font: "Auto",        //  what font HTML-CSS should use
    context: "MathJax",  //  or "Browser" for pass-through to browser menu
    mpContext: false,    //  true means pass menu events to MathPlayer in IE
    mpMouse: false,      //  true means pass mouse events to MathPlayer in IE
    texHints: true       //  include class names for TeXAtom elements
  },
  

  errorSettings: {
    message: ["[Math Processing Error]"], // HTML snippet structure for message to use
    messageId: "MathProcessingError",     // ID of snippet for localization
    style: {color: "#CC0000", "font-style":"italic"}  // style for message
  }
});

MathJax.Ajax.loadComplete("[MathJax]/config/default.js");
