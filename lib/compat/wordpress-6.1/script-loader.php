<?php
/**
 * Load global styles assets in the front-end.
 *
 * @package gutenberg
 */

/**
 * This function takes care of adding inline styles
 * in the proper place, depending on the theme in use.
 *
 * The method was added to core in 5.9.1, but with with a single param ($style). The second param ($priority) was
 * added post 6.0 so the 6.1 release needs to have wp_enqueue_block_support_styles update to include this param.
 *
 * For block themes, it's loaded in the head.
 * For classic ones, it's loaded in the body
 * because the wp_head action  happens before
 * the render_block.
 *
 * @link https://core.trac.wordpress.org/ticket/53494.
 *
 * @param string $style String containing the CSS styles to be added.
 * @param int    $priority To set the priority for the add_action.
 */
function gutenberg_enqueue_block_support_styles( $style, $priority = 10 ) {
	$action_hook_name = 'wp_footer';
	if ( wp_is_block_theme() ) {
		$action_hook_name = 'wp_head';
	}
	add_action(
		$action_hook_name,
		static function () use ( $style ) {
			echo "<style>$style</style>\n";
		},
		$priority
	);
}
