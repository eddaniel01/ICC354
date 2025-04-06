import{R as e,e as i,N as d,b as p,O as a}from"./indexhtml-fcd2fc70.js";import{labelProperties as s,helperTextProperties as l,errorMessageProperties as n}from"./vaadin-text-field-0b3db014-202ef189.js";const c={tagName:"vaadin-radio-group",displayName:"Radio Button Group",elements:[{selector:"vaadin-radio-group",displayName:"Group",properties:[e.backgroundColor,e.borderColor,e.borderWidth,e.borderRadius,e.padding]},{selector:"vaadin-radio-group::part(label)",displayName:"Label",properties:s},{selector:"vaadin-radio-group::part(helper-text)",displayName:"Helper text",properties:l},{selector:"vaadin-radio-group::part(error-message)",displayName:"Error message",properties:n},{selector:"vaadin-radio-group vaadin-radio-button",displayName:"Radio buttons",properties:[{propertyName:"--vaadin-radio-button-size",displayName:"Radio button size",defaultValue:"var(--lumo-font-size-l)",editorType:i.range,presets:d.lumoFontSize,icon:"square"}]},{selector:"vaadin-radio-group vaadin-radio-button::part(radio)",displayName:"Radio part",properties:[e.backgroundColor,e.borderColor,e.borderWidth]},{selector:"vaadin-radio-group vaadin-radio-button[checked]::part(radio)",stateAttribute:"checked",stateElementSelector:"vaadin-radio-group vaadin-radio-button",displayName:"Radio part (when checked)",properties:[e.backgroundColor,e.borderColor,e.borderWidth]},{selector:"vaadin-radio-group vaadin-radio-button::part(radio)::after",displayName:"Selection indicator",properties:[{...p.iconColor,propertyName:"border-color"}]},{selector:"vaadin-radio-group vaadin-radio-button label",displayName:"Label",properties:[a.textColor,a.fontSize,a.fontWeight,a.fontStyle]}],setupElement(t){const o=document.createElement("vaadin-radio-button"),r=document.createElement("label");r.textContent="Some label",r.setAttribute("slot","label"),o.append(r),t.append(o)}};export{c as default};
