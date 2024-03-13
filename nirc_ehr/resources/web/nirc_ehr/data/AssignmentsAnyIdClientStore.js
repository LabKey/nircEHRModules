Ext4.define('NIRC_EHR.data.AssignmentsAnyIdClientStore', {
    extend: 'NIRC_EHR.data.AssignmentsClientStore',

    getExtraContext: function () {
        var ret = this.callParent(arguments) || {};

        // Tell the trigger scripts to allow any Id. Requires handling in trigger script to fully enable.
        ret['allowAnyId'] = true;
        return ret;
    }
});