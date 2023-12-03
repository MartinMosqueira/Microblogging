<template>
  <div>
    <b-dropdown-item to="/application-user">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.applicationUser')"></span>
    </b-dropdown-item>
    <b-dropdown-item to="/mensaje-muro">
      <font-awesome-icon icon="asterisk" />
      <span v-text="t$('global.menu.entities.mensajeMuro')"></span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
